package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.CommodityDto;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Commodity;
import lv.pakit.repo.ICommodityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommodityService {

    private final ICommodityRepo commodityRepo;

    public void create(CommodityDto dto) {
        Commodity commodity = mapToCommodity(dto);
        commodityRepo.save(commodity);
    }

    public CommodityDto retrieveById(int id) {
        Commodity commodity = requireCommodityById(id);

        return mapToDto(commodity);
    }

    public List<CommodityDto> retrieveAll() {
        return commodityRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void updateById(int id, Commodity dto) {
        Commodity commodity = requireCommodityById(id);

        commodity.setCommodityCode(dto.getCommodityCode());
        commodity.setDescription(dto.getDescription());
        commodityRepo.save(commodity);
    }

    public void deleteById(int id) {
        requireCommodityById(id);
        commodityRepo.deleteById(id);
    }

    public CommodityDto mapToDto(Commodity commodity) {
        return CommodityDto.builder()
                .commodityCode(commodity.getCommodityCode())
                .description(commodity.getDescription())
                .build();
    }

    private Commodity mapToCommodity(CommodityDto dto) {
        return Commodity.builder()
                .commodityCode(dto.getCommodityCode())
                .description(dto.getDescription())
                .build();
    }

    private Commodity requireCommodityById(int id) {
        return commodityRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("PackageItem with id (" + id + ") not found!"));
    }
}
