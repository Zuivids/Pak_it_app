package lv.pakit.service;

import lv.pakit.exception.NotFoundException;
import lv.pakit.dto.PackageItemDto;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IPackageItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageItemService {

    @Autowired
    private IPackageItemRepo packageItemRepo;

    public void create(PackageItemDto dto) {
        PackageItem item = mapToPackageItem(dto);
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

        item.setCommodityId(dto.getCommodityId());
        item.setDeclarationId(dto.getDeclarationId());
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
                .id(item.getPackageItemId())
                .commodityId(item.getCommodityId())
                .declarationId(item.getDeclarationId())
                .quantity(item.getQuantity())
                .netWeight(item.getNetWeight())
                .value(item.getValue())
                .used(item.isUsed())
                .build();
    }

    private PackageItem mapToPackageItem(PackageItemDto dto) {
        return PackageItem.builder()
                .packageItemId(dto.getId())
                .commodityId(dto.getCommodityId())
                .declarationId(dto.getDeclarationId())
                .quantity(dto.getQuantity())
                .netWeight(dto.getNetWeight())
                .value(dto.getValue())
                .used(dto.isUsed())
                .build();
    }

    private PackageItem requirePackageItemById(int id) {
        return packageItemRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("PackageItem with id (" + id + ") not found!"));
    }
}
