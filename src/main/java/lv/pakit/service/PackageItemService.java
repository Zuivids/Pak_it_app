package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.packageItem.PackageItemRequest;
import lv.pakit.dto.response.PackageItemResponse;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IPackageItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageItemService {

    private final IPackageItemRepo packageItemRepo;

    public void create(PackageItemRequest request) {
        PackageItem packageItem = PackageItem.builder()
                .commodity(request.getCommodity())
                .declaration(request.getDeclarationId())
                .quantity(request.getQuantity())
                .netWeight(request.getNetWeight())
                .value(request.getValue())
                .used(request.isUsed())
                .build();

        packageItemRepo.save(packageItem);
    }

    public void updateById(long id, PackageItemRequest packageItemRequest) {
        PackageItem packageItem = requirePackageItemById(id);

        packageItem.setQuantity(packageItemRequest.getQuantity());
        packageItem.setNetWeight(packageItemRequest.getNetWeight());
        packageItem.setValue(packageItemRequest.getValue());
        packageItem.setUsed(packageItemRequest.isUsed());

        packageItemRepo.save(packageItem);
    }

    public PackageItemResponse fetchById(long id) {
        return mapToDto(requirePackageItemById(id));
    }

    public List<PackageItemResponse> retrieveByDeclarationId(long declarationId) {
        List<PackageItem> items = packageItemRepo.findByDeclarationDeclarationId(declarationId);

        return items.stream().map(this::mapToDto).toList();
    }

    public List<PackageItemResponse> retrieveAll() {
        return packageItemRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteById(long id) {
        requirePackageItemById(id);
        packageItemRepo.deleteById(id);
    }


    private PackageItem requirePackageItemById(long id) {
        return packageItemRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("PackageItem with id (" + id + ") not found!"));
    }

    public PackageItemResponse mapToDto(PackageItem packageItem) {
        return PackageItemResponse.builder()
                .packageItemId(packageItem.getPackageItemId())
                .quantity(packageItem.getQuantity())
                .netWeight(packageItem.getNetWeight())
                .value(packageItem.getValue())
                .used(packageItem.isUsed())
                .commodity(packageItem.getCommodity())
                .declarationId(packageItem.getDeclaration().getDeclarationId())
                .build();
    }
}
