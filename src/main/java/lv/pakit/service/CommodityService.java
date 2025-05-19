package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.CommodityDto;
import lv.pakit.dto.request.CommodityRequest;
import lv.pakit.exception.NotFoundException;
import lv.pakit.exception.PakItException;
import lv.pakit.model.Commodity;
import lv.pakit.repo.ICommodityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommodityService {

    private final ICommodityRepo commodityRepo;

    public void create(CommodityRequest commodityRequest) {
        Commodity commodity = mapToCommodity(commodityRequest);
        throw new PakItException("Something went wrong!");
//        commodityRepo.save(commodity);
    }

    public CommodityDto retrieveById(long id) {
        Commodity commodity = requireCommodityById(id);

        return mapToDto(commodity);
    }

    public List<CommodityDto> retrieveAll() {
        return commodityRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void updateById(long id, CommodityRequest commodityRequest) {
        Commodity commodity = requireCommodityById(id);

        commodity.setCommodityCode(commodityRequest.getCommodityCode());
        commodity.setDescription(commodityRequest.getDescription());

        commodityRepo.save(commodity);
    }

    public void deleteById(long id) {
        requireCommodityById(id);
        commodityRepo.deleteById(id);
    }

    public CommodityDto mapToDto(Commodity commodity) {
        return CommodityDto.builder()
                .commodityId(commodity.getCommodityId())
                .commodityCode(commodity.getCommodityCode())
                .description(commodity.getDescription())
                .build();
    }

    private Commodity mapToCommodity(CommodityRequest commodityRequest) {
        return Commodity.builder()
                .commodityCode(commodityRequest.getCommodityCode())
                .description(commodityRequest.getDescription())
                .build();
    }

    private Commodity requireCommodityById(long id) {
        return commodityRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Commodity with id (" + id + ") not found!"));
    }

    public List<CommodityDto> search(String query) {
        if (query == null || query.isEmpty()) {
            return commodityRepo.findAll().stream()
                    .map(this::mapToDto)
                    .toList();
        }

        List<Commodity> commodities = commodityRepo
                .findByCommodityCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

        return commodities.stream()
                .map(this::mapToDto)
                .toList();
    }
}
