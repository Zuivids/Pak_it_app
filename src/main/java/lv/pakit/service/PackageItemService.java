package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.PackageItemRequest;
import lv.pakit.exception.NotFoundException;
import lv.pakit.dto.PackageItemDto;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IPackageItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageItemService {

    private final IPackageItemRepo packageItemRepo;
    private final CommodityService commodityService;

    public void create(PackageItemRequest packageItemRequest) {
        PackageItem item = mapToPackageItem(packageItemRequest);
        packageItemRepo.save(item);
    }

    public PackageItemDto retrieveById(int id) {
        PackageItem item = requirePackageItemById(id);

        return mapToDto(item);
    }

    public List<PackageItemDto> retrieveAll() {
        return packageItemRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void updateById(int id, PackageItemDto dto) {
        PackageItem item = requirePackageItemById(id);

        item.setQuantity(dto.getQuantity());
        item.setNetWeight(dto.getNetWeight());
        item.setValue(dto.getValue());
        item.setUsed(dto.isUsed());

        packageItemRepo.save(item);
    }

    public void deleteById(int id) {
        requirePackageItemById(id);
        packageItemRepo.deleteById(id);
    }

    private PackageItemDto mapToDto(PackageItem item) {
        return PackageItemDto.builder()
                .packageItemId(item.getPackageItemId())
                .quantity(item.getQuantity())
                .netWeight(item.getNetWeight())
                .value(item.getValue())
                .used(item.isUsed())
                .commodity(commodityService.mapToDto(item.getCommodity()))
                .build();
    }

    private PackageItem mapToPackageItem(PackageItemRequest packageItemRequest) {
        return PackageItem.builder()
                .quantity(packageItemRequest.getQuantity())
                .netWeight(packageItemRequest.getNetWeight())
                .value(packageItemRequest.getValue())
                .used(packageItemRequest.getUsed())
                .build();
    }

    private PackageItem requirePackageItemById(int id) {
        return packageItemRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("PackageItem with id (" + id + ") not found!"));
    }
}
