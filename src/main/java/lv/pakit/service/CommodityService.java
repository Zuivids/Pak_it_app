package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.exception.http.FieldErrorException;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.Commodity;
import lv.pakit.repo.ICommodityRepo;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommodityService {

    private final ICommodityRepo commodityRepo;

    public void create(CommodityRequest request) {
        checkCommodityCodeIsUnique(request);

        Commodity commodity = Commodity.builder()
                .commodityCode(request.getCommodityCode())
                .description(request.getDescription())
                .build();

        commodityRepo.save(commodity);
    }

    public void updateById(long id, CommodityRequest request) {
        Commodity commodity = requireById(id);

        if (commmodityCodeChanged(commodity, request)) {
            checkCommodityCodeIsUnique(request);
        }

        commodity.setCommodityCode(request.getCommodityCode());
        commodity.setDescription(request.getDescription());

        commodityRepo.save(commodity);
    }

    private boolean commmodityCodeChanged(Commodity commodity, CommodityRequest request) {
        return !request.getCommodityCode().equals(commodity.getCommodityCode());
    }

    private void checkCommodityCodeIsUnique(CommodityRequest request) {
        if (commodityRepo.findByCommodityCode(request.getCommodityCode()).isPresent()) {
            String errorMsg = "Commodity with code '" + request.getCommodityCode() + "' already exists.";
            throw new FieldErrorException("commodityCode", errorMsg);
        }
    }

    public void deleteById(long id) {
        commodityRepo.deleteById(id);
    }

    public CommodityResponse fetchById(long id) {
        return mapToDto(requireById(id));
    }

    public List<CommodityResponse> fetchByQuery(String query) {
        if (query == null || query.isEmpty()) {
            return fetchAll();
        }

        List<Commodity> commodities = commodityRepo
                .findByCommodityCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

        return commodities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<CommodityResponse> fetchAll() {
        return commodityRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public Commodity requireById(long id) {
        return commodityRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Commodity with id (" + id + ") not found!"));
    }

    public CommodityResponse mapToDto(Commodity commodity) {
        return CommodityResponse.builder()
                .commodityId(commodity.getCommodityId())
                .commodityCode(commodity.getCommodityCode())
                .description(commodity.getDescription())
                .build();
    }

    public void importFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            DataFormatter formatter = new DataFormatter(Locale.US);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
                Sheet sheet = workbook.getSheetAt(s);
                if (sheet == null) continue;

                int rowNum = 0;
                for (Row row : sheet) {
                    rowNum++;
                    if (rowNum == 1) continue;

                    Cell codeCell = row.getCell(0); // commodity code
                    Cell descCell = row.getCell(5); // description

                    String code = readCommodityCode(codeCell, formatter, evaluator);
                    String description = readCellAsString(descCell, formatter, evaluator);

                    if (code.isEmpty() || description.isEmpty()) {
                        continue;
                    }

                    if (!code.matches("\\d{10}")) {
                        System.err.println("Skipping row " + rowNum + " in sheet '" + sheet.getSheetName() +
                                "': invalid commodity code '" + code + "'");
                        continue;
                    }

                    CommodityRequest req = new CommodityRequest();
                    req.setCommodityCode(code);
                    req.setDescription(description);

                    try {
                        create(req);
                    } catch (Exception e) {
                        System.err.println("Skipping row " + rowNum + " in sheet '" + sheet.getSheetName() +
                                "', code " + code + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file", e);
        }
    }

    private String readCommodityCode(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        String v = readCellAsString(cell, formatter, evaluator);
        if (v.isEmpty()) return "";

        v = v.replaceAll("[\\s,]", "");

        if (v.matches("(?i)[+-]?(?:\\d+\\.?\\d*|\\.\\d+)[eE][+-]?\\d+")) {
            try {
                v = new BigDecimal(v).toPlainString();
            } catch (NumberFormatException ignore) {}
        }

        return v.replaceAll("\\D", "");
    }

    private String readCellAsString(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        if (cell == null) return "";
        return formatter.formatCellValue(cell, evaluator).trim();
    }

}
