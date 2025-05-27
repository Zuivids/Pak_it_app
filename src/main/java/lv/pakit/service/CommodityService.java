package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.exception.FieldErrorException;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Commodity;
import lv.pakit.repo.ICommodityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

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
}