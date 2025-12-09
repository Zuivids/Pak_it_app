package lv.pakit.service.shipment;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.response.shipment.ShipmentStatsResponse;
import lv.pakit.dto.response.shipment.ShipmentDeclarationStats;
import lv.pakit.dto.response.shipment.ShipmentCommodityStats;
import lv.pakit.model.shipment.Shipment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShipmentStatsExportService {

    private final ShipmentStatsService shipmentStatsService;
    private final ShipmentService shipmentService;

    public void getShipmentStatsPdf(long shipmentId, HttpServletResponse response) {
        try {
            ShipmentStatsResponse stats = shipmentStatsService.getShipmentStats(shipmentId);
            Shipment shipment = shipmentService.requireById(shipmentId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            addTitle(document, "Shipment Report - ID: " + shipment.getShipmentCode());
            addGeneralInfo(document, stats);
            addTotals(document, stats);
            addDeclarationsTable(document, stats.getDeclarationStats());
            addCommoditiesTable(document, stats.getCommodityStats());
//            addDate(document);

            document.close();

            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=shipment_" + shipmentId + ".pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("Failed to generate shipment PDF for id={}", shipmentId, e);
            throw new RuntimeException("Failed to generate shipment PDF");
        }
    }

    private void addTitle(Document document, String titleText) throws DocumentException {
        Paragraph title = new Paragraph(titleText,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));
    }

    private void addGeneralInfo(Document document, ShipmentStatsResponse stats) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{1, 2});
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        document.add(table);
    }

    private void addTotals(Document document, ShipmentStatsResponse stats) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{1, 2});
        table.setSpacingBefore(5f);
        table.setSpacingAfter(10f);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        addCell(table, "Total weight:", true);
        addCell(table, stats.getTotalWeight() + " Kg", false);

        addCell(table, "Total value:", true);
        addCell(table, stats.getTotalValue() + " EUR", false);

        addCell(table, "Packages:", true);
        addCell(table, stats.getTotalPackageAmount() + "", false);

        document.add(table);
    }

    private void addDeclarationsTable(Document document, List<ShipmentDeclarationStats> declarations) throws DocumentException {
        document.add(new Paragraph("Declarations", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2, 2, 2});

        addTableHeader(table, "", "Declaration code", "Total weight (Kg)", "Total value (EUR)", "Package Amount");

        int index = 1;
        for (ShipmentDeclarationStats d : declarations) {
            table.addCell(String.valueOf(index++));
            table.addCell(d.getIdentifierCode());
            table.addCell(String.valueOf(d.getTotalWeight()));
            table.addCell(String.valueOf(d.getTotalValue()));
            table.addCell(String.valueOf(d.getPackageAmount()));
        }
        document.add(table);
    }

    private void addCommoditiesTable(Document document, List<ShipmentCommodityStats> commodities) throws DocumentException {
        document.add(new Paragraph("\nCommodities", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2, 2, 2});

        addTableHeader(table, "", "Commodity code", "Quantity", "Total weight (Kg)", "Total value (EUR)");

        int index = 1;
        for (ShipmentCommodityStats c : commodities) {
            table.addCell(String.valueOf(index++));
            table.addCell(c.getCommodityCode() + " - " + c.getDescription());
            table.addCell(String.valueOf(c.getQuantity()));
            table.addCell(String.valueOf(c.getTotalWeight()));
            table.addCell(String.valueOf(c.getTotalValue()));
        }

        document.add(table);
    }

    private void addDate(Document document) throws DocumentException {
        document.add(new Paragraph("\n"));
        String date = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        Paragraph dateParagraph = new Paragraph("Date: " + date, FontFactory.getFont(FontFactory.HELVETICA, 12));
        document.add(dateParagraph);
    }

    private void addCell(PdfPTable table, String text, boolean bold) {
        Font font = bold ? FontFactory.getFont(FontFactory.HELVETICA_BOLD) :
                FontFactory.getFont(FontFactory.HELVETICA);
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
}
