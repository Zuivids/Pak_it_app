package lv.pakit.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.exception.http.FieldErrorException;
import lv.pakit.exception.http.InternalErrorException;
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
@Slf4j
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

    @Transactional
    public void uploadCommodities(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file is empty");
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            parseCommodityFile(workbook);
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }
    }

    private void parseCommodityFile(Workbook workbook) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
            Sheet sheet = workbook.getSheetAt(s);
            if (sheet == null) {
                continue;
            }

            parseCommoditySheet(sheet, formatter, evaluator);
        }
    }

    private void parseCommoditySheet(Sheet sheet, DataFormatter formatter, FormulaEvaluator evaluator) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            try {
                parseCommodityRow(row, formatter, evaluator);
            } catch (Exception e) {
                log.error("Failed to parse commodity row {}", row.getRowNum(), e);
            }
        }
    }

    private void parseCommodityRow(Row row, DataFormatter formatter, FormulaEvaluator evaluator) {
        final Cell codeCell = row.getCell(0); // commodity code
        final Cell descCell = row.getCell(5); // description
        final String code = readCommodityCode(codeCell, formatter, evaluator);
        final String description = readCellAsString(descCell, formatter, evaluator);

        if (code.isEmpty() || description.isEmpty()) {
            log.error("Skipping commodity row {} due to missing values", row.getRowNum());
            return;
        }
        if (!code.matches("\\d{10}")) {
            log.error("Skipping commodity row {} due to malformed commodity code {}", row.getRowNum(), code);
            return;
        }

        if (commodityRepo.findByCommodityCode(code).isPresent()) {
            log.warn("Skipping duplicate commodity code: {}", code);
            return;
        }

        commodityRepo.save(Commodity.builder()
                .commodityCode(code)
                .description(description)
                .build());
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
