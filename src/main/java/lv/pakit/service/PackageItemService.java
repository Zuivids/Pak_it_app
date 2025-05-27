package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.packageItem.PackageItemRequest;
import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.dto.response.PackageItemResponse;
import lv.pakit.model.Commodity;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IPackageItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageItemService {

    private final CommodityService commodityService;
    private final IPackageItemRepo packageItemRepo;

    public List<PackageItemResponse> fetchByDeclarationId(long declarationId) {
        return packageItemRepo.findByDeclarationId(declarationId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public void createAll(long declarationId, List<PackageItemRequest> requests) {
        List<PackageItem> packageItems = requests.stream()
                .map((packageItemRequest -> mapFromDto(declarationId, packageItemRequest)))
                .toList();

        packageItemRepo.saveAll(packageItems);
    }

    private PackageItemResponse mapToDto(PackageItem packageItem) {
        CommodityResponse commodityResponse = commodityService.mapToDto(packageItem.getCommodity());

        return PackageItemResponse.builder()
                .packageItemId(packageItem.getPackageItemId())
                .declarationId(packageItem.getDeclarationId())
                .commodity(commodityResponse)
                .quantity(packageItem.getQuantity())
                .netWeight(packageItem.getNetWeight())
                .value(packageItem.getValue())
                .used(packageItem.isUsed())
                .build();
    }

    private PackageItem mapFromDto(long declarationId, PackageItemRequest request) {
        Commodity commodity = commodityService.requireById(request.getCommodityId());

        return PackageItem.builder()
                .commodity(commodity)
                .declarationId(declarationId)
                .quantity(request.getQuantity())
                .netWeight(request.getNetWeight())
                .value(request.getValue())
                .used(request.isUsed())
                .build();
    }

    public double calculateTotalWeight(List<PackageItemRequest> requests) {
        return requests.stream()
                .mapToDouble(PackageItemRequest::getNetWeight)
                .sum();
    }

    public double calculateTotalValue(List<PackageItemRequest> requests) {
        return requests.stream()
                .mapToDouble(PackageItemRequest::getValue)
                .sum();
    }
}