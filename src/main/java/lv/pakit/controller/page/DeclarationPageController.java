package lv.pakit.controller.page;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import com.lowagie.text.pdf.PdfPTable;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.service.ClientService;
import lv.pakit.service.CommodityService;
import lv.pakit.service.DeclarationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class DeclarationPageController {

    private final DeclarationService declarationService;
    private final CommodityService commodityService;
    private final ClientService clientService;

    @GetMapping("/declaration")
    public String getAllDeclarations(@ModelAttribute(value = "query") DeclarationSearchRequest request,
                                     BindingResult bindingResult, Model model) {
        model.addAttribute("declarations", declarationService.search(request));

        return "declaration-show-many-page";
    }

    @GetMapping("/declaration/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        addDeclarationToModel(id, model);
        return "declaration-show-one-page";
    }

    @GetMapping("/declaration/new")
    public String showDeclarationCreateForm(Model model) {
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());

        return "declaration-add-new-page";
    }

    @GetMapping("/declaration/{id}/edit")
    public String showDeclarationEditForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());

        return "declaration-edit-page";
    }

    @GetMapping("/declaration/{id}/delete")
    public String showDeclarationDeleteForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);

        return "declaration-delete-page";
    }

    private void addDeclarationToModel(long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
    }

    @GetMapping("/declaration/{id}/pdf")
    public void downloadPdf(@PathVariable long id, HttpServletResponse response) throws IOException, DocumentException {
        var declaration = declarationService.fetchById(id);

        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=declaration_" + id + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // --- Title ---
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

        // --- Outer table: single table for all rows ---
        PdfPTable outerTable = new PdfPTable(4);
        outerTable.setWidthPercentage(100);
        outerTable.setWidths(new float[]{3, 1, 1, 2});

        // 1. Row: Consignment info spanning all columns
        PdfPCell consignmentCell = new PdfPCell(new Paragraph(
                "Consignment information (Packages): " + declaration.getPackageItems().size() + " units",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        ));
        consignmentCell.setColspan(4);
        outerTable.addCell(consignmentCell);

        // 2. Row: Sender | Receiver (two cells, each spans 2 columns)
        PdfPCell senderCell = new PdfPCell();
        senderCell.addElement(new Paragraph("Consignor (Sender): " + declaration.getSenderName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        senderCell.addElement(new Paragraph("Address: " + declaration.getSenderAddress()));
        senderCell.addElement(new Paragraph("Tel: " + declaration.getSenderPhoneNumber()));
        senderCell.setColspan(2);
        outerTable.addCell(senderCell);

        PdfPCell receiverCell = new PdfPCell();
        receiverCell.addElement(new Paragraph("Consignee (Receiver): " + declaration.getReceiverName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        receiverCell.addElement(new Paragraph("Address: " + declaration.getReceiverAddress()));
        receiverCell.addElement(new Paragraph("Tel: " + declaration.getReceiverPhoneNumber()));
        receiverCell.setColspan(2);
        outerTable.addCell(receiverCell);

        // 3. Header row for items
        outerTable.addCell(new PdfPCell(new Paragraph("Detailed Description of goods", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        outerTable.addCell(new PdfPCell(new Paragraph("Net Mass", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        outerTable.addCell(new PdfPCell(new Paragraph("Value", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        outerTable.addCell(new PdfPCell(new Paragraph("Commodity Code", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

        // 4. Data rows for items
        for (var item : declaration.getPackageItems()) {
            outerTable.addCell(item.getCommodity().getDescription());
            outerTable.addCell(String.valueOf(item.getNetWeight()));
            outerTable.addCell(String.valueOf(item.getValue()));
            outerTable.addCell(item.getCommodity().getCommodityCode());
        }

        // 5. Totals row (Total Weight | Total Value)
        PdfPCell totalWeightCell = new PdfPCell(new Paragraph("Total Weight: " + declaration.getTotalWeight(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        totalWeightCell.setColspan(2);
        totalWeightCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.addCell(totalWeightCell);

        PdfPCell totalValueCell = new PdfPCell(new Paragraph("Total Value: " + declaration.getTotalValue(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        totalValueCell.setColspan(2);
        totalValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.addCell(totalValueCell);

        // 6. Consignment | date
        PdfPCell Consignment = new PdfPCell(new Paragraph("I certify that the particulars given in this Consignment Note and Customs Declaration are correct and that above listed items do not contain any dangerous article or articles prohibited by legislation or by postal or customs regulations."));
        Consignment.setColspan(2);
        outerTable.addCell(Consignment);

        PdfPCell date = new PdfPCell(new Paragraph("Date: " + declaration.getDate()));
        date.setColspan(2);
        outerTable.addCell(date);

        // 7. ConsignmentPakIt | sendersSignature
        PdfPCell ConsignmentPakIt = new PdfPCell(new Paragraph("I instruct “SIA PaKit” and its legal representative to act on my behalf in connection with the presentation and completion of all documents (including those for customs purposes) relating to the movement and declaration of the items listed above." + declaration.getTotalValue(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        ConsignmentPakIt.setColspan(2);
        outerTable.addCell(ConsignmentPakIt);

        PdfPCell sendersSignature = new PdfPCell(new Paragraph("Sender’s signature:"));
        sendersSignature.setColspan(2);
        outerTable.addCell(sendersSignature);

        document.add(outerTable);
        document.close();
    }


}
