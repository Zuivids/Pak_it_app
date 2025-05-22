//package lv.pakit.service;
//
//import lombok.RequiredArgsConstructor;
//import lv.pakit.exception.NotFoundException;
//import lv.pakit.dto.PackageItemDto;
//import lv.pakit.model.PackageItem;
//import lv.pakit.repo.IPackageItemRepo;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PackageItemService {
//
//    private final IPackageItemRepo packageItemRepo;
//    private final CommodityService commodityService;
//
//    public void create(PackageItemDto packageItemDto) {
//        PackageItem item = mapToPackageItem(packageItemDto);
//        packageItemRepo.save(item);
//    }
//
//    public PackageItemDto retrieveById(long id) {
//        PackageItem item = requirePackageItemById(id);
//
//        return mapToDto(item);
//    }
//
//    public List<PackageItemDto> retrieveByDeclarationId(long declarationId) {
//        List<PackageItem> items = packageItemRepo.findByDeclaration_DeclarationId(declarationId);
//
//        return items.stream().map(this::mapToDto).toList();
//    }
//
//    public List<PackageItemDto> retrieveAll() {
//        return packageItemRepo.findAll().stream()
//                .map(this::mapToDto)
//                .toList();
//    }
//
//    public void updateById(long id, PackageItemDto packageItemDto) {
//        PackageItem item = requirePackageItemById(id);
//
//        item.setQuantity(packageItemDto.getQuantity());
//        item.setNetWeight(packageItemDto.getNetWeight());
//        item.setValue(packageItemDto.getValue());
//        item.setUsed(packageItemDto.isUsed());
//
//        packageItemRepo.save(item);
//    }
//
//    public void deleteById(long id) {
//        requirePackageItemById(id);
//        packageItemRepo.deleteById(id);
//    }
//
//    public PackageItemDto mapToDto(PackageItem item) {
//        return PackageItemDto.builder()
//                .packageItemId(item.getPackageItemId())
//                .quantity(item.getQuantity())
//                .netWeight(item.getNetWeight())
//                .value(item.getValue())
//                .used(item.isUsed())
//                .commodity(commodityService.mapToDto(item.getCommodity()))
//                .declarationId(item.getDeclaration().getDeclarationId())
//                .build();
//    }
//
//    private PackageItem mapToPackageItem(PackageItemDto packageItemDto) {
//        return PackageItem.builder()
//                .quantity(packageItemDto.getQuantity())
//                .netWeight(packageItemDto.getNetWeight())
//                .value(packageItemDto.getValue())
//                .used(packageItemDto.isUsed())
//                .build();
//    }
//
//    private PackageItem requirePackageItemById(long id) {
//        return packageItemRepo.findById(id)
//                .orElseThrow(() -> new NotFoundException("PackageItem with id (" + id + ") not found!"));
//    }
//}
