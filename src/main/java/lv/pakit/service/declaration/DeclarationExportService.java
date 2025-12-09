package lv.pakit.service.declaration;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.exception.PakItException;
import lv.pakit.exception.http.InternalErrorException;
import lv.pakit.model.Declaration;
import lv.pakit.service.PackageItemService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeclarationExportService {

    private final DeclarationService declarationService;
    private final PackageItemService packageItemService;

    public void getDeclarationPdf(long declarationId, HttpServletResponse httpResponse) {
        try {
            byte[] pdfBytes = generatePdf(declarationId);
            writePdfResponse(pdfBytes, httpResponse, declarationId);
        } catch (PakItException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to generate PDF for declaration: id={}", declarationId, e);
            throw new InternalErrorException("Failed to generate PDF for declaration ");
        }
    }

    public byte[] generatePdf(long declarationId) throws Exception {
        var declaration = declarationService.requireById(declarationId);

        try (var baos = new java.io.ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            addTitle(document);
            document.add(buildOuterTable(declarationId, declaration));
//            addDate(document);

            document.close();
            return baos.toByteArray();
        }
    }

    public void writePdfResponse(byte[] pdfBytes, HttpServletResponse response, long declarationId) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=declaration_" + declarationId + ".pdf");

        try (var os = response.getOutputStream()) {
            os.write(pdfBytes);
            os.flush();
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph(
                "Consignment Note & Customs Declaration",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This form must be completed by the consignor or their representative before the items can be accepted for shipment."));
        document.add(new Paragraph("Incomplete information may delay shipment of your goods and there are severe penalties for making false declaration to Customs."));
        document.add(new Paragraph("\n"));
    }

    private PdfPTable buildOuterTable(long declarationId, Declaration declaration) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 2, 2, 2, 2, 2});

        addConsignmentInfo(table, declarationId);
        addSenderReceiver(table, declaration);
        addItemsHeader(table);
        addItems(table, declarationId);
        addTotals(table, declaration);
        addCertification(table);

        return table;
    }

    private void addConsignmentInfo(PdfPTable table, long declarationId) {
        var declaration = declarationService.requireById(declarationId);

        PdfPCell consignmentCell = new PdfPCell(new Paragraph(
                "Consignment information (Packages): " + declaration.getPackageAmount() + " units",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        ));
        consignmentCell.setColspan(6);
        table.addCell(consignmentCell);
    }

    private void addSenderReceiver(PdfPTable table, Declaration declaration) {
        PdfPCell senderCell = new PdfPCell();
        senderCell.addElement(new Paragraph("Consignor (Sender): " + declaration.getSenderName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        senderCell.addElement(new Paragraph("Address: " + declaration.getSenderAddress()));
        senderCell.addElement(new Paragraph("Tel: " + declaration.getSenderPhoneNumber()));
        senderCell.setColspan(3);
        table.addCell(senderCell);

        PdfPCell receiverCell = new PdfPCell();
        receiverCell.addElement(new Paragraph("Consignee (Receiver): " + declaration.getReceiverName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        receiverCell.addElement(new Paragraph("Address: " + declaration.getReceiverAddress()));
        receiverCell.addElement(new Paragraph("Tel: " + declaration.getReceiverPhoneNumber()));
        receiverCell.setColspan(3);
        table.addCell(receiverCell);
    }

    private void addItemsHeader(PdfPTable table) {
        PdfPCell descCell = new PdfPCell(new Paragraph("Detailed Description of goods",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        descCell.setColspan(2);
        table.addCell(descCell);
        table.addCell(new PdfPCell(new Paragraph("Quantity", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        table.addCell(new PdfPCell(new Paragraph("Net Mass", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        table.addCell(new PdfPCell(new Paragraph("Value", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        table.addCell(new PdfPCell(new Paragraph("Commodity Code", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
    }

    private void addItems(PdfPTable table, long declarationId) {
        for (var item : packageItemService.fetchByDeclarationId(declarationId)) {
            PdfPCell descCell = new PdfPCell(new Paragraph(item.getCommodity().getDescription()));
            descCell.setColspan(2);
            table.addCell(descCell);
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.valueOf(item.getNetWeight()));
            table.addCell(String.valueOf(item.getValue()));
            table.addCell(item.getCommodity().getCommodityCode());
        }
    }

    private void addTotals(PdfPTable table, Declaration declaration) {
        PdfPCell totalWeightCell = new PdfPCell(new Paragraph(
                "Total Weight: " + declaration.getTotalWeight(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        ));
        totalWeightCell.setColspan(3);
        totalWeightCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(totalWeightCell);

        PdfPCell totalValueCell = new PdfPCell(new Paragraph(
                "Total Value: " + declaration.getTotalValue(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        ));
        totalValueCell.setColspan(3);
        totalValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(totalValueCell);
    }

    private void addCertification(PdfPTable table){

        PdfPCell consignment = new PdfPCell(new Paragraph(
                "[x] I certify that the particulars given in this Consignment Note and Customs Declaration are correct and that above listed items do not contain any dangerous article or articles prohibited by legislation or by postal or customs regulations."
        ));
        consignment.setColspan(6);
        table.addCell(consignment);

        PdfPCell agreementCell = new PdfPCell(new Paragraph(
                "[x] I instruct “SIA PaKit” and its legal representative to act on my behalf in connection with the presentation and completion of all documents (including those for customs purposes) relating to the movement and declaration of the items listed above."
        ));
        agreementCell.setColspan(6);
        table.addCell(agreementCell);
    }

    private void addDate(Document document) throws DocumentException {
        document.add(new Paragraph("\n"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = java.time.LocalDate.now().format(fmt);

        Paragraph dateParagraph = new Paragraph(
                "Date: " + formattedDate,
                FontFactory.getFont(FontFactory.HELVETICA, 12)
        );

        document.add(dateParagraph);
    }
}
