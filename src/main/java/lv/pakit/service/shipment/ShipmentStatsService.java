package lv.pakit.service.shipment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.response.shipment.ShipmentCommodityStats;
import lv.pakit.dto.response.shipment.ShipmentDeclarationStats;
import lv.pakit.dto.response.shipment.ShipmentStatsResponse;
import lv.pakit.model.Commodity;
import lv.pakit.model.Declaration;
import lv.pakit.repo.IDeclarationRepo;
import lv.pakit.repo.IPackageItemRepo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentStatsService {

    private final ShipmentService shipmentService;
    private final IDeclarationRepo declarationRepo;
    private final IPackageItemRepo packageItemRepo;

    public ShipmentStatsResponse getShipmentStats(long id) {
        shipmentService.requireById(id);

        List<Declaration> declarations = declarationRepo.findByShipmentId(id);

        return ShipmentStatsResponse.builder()
                .totalWeight(calculateTotalWeight(declarations))
                .totalValue(calculateTotalValue(declarations))
                .declarationStats(getDeclarationStats(declarations))
                .commodityStats(getCommodityStats(declarations))
                .build();
    }

    private double calculateTotalWeight(List<Declaration> declarations) {
        return declarations.stream()
                .mapToDouble(Declaration::getTotalWeight).sum();
    }

    private double calculateTotalValue(List<Declaration> declarations) {
        return declarations.stream()
                .mapToDouble(Declaration::getTotalValue).sum();
    }

    private List<ShipmentDeclarationStats> getDeclarationStats(List<Declaration> declarations) {
        return declarations.stream()
                .map(this::mapShipmentDeclarationStats)
                .toList();
    }

    private List<ShipmentCommodityStats> getCommodityStats(List<Declaration> declarations) {
        Map<Long, Double> totalCommodityWeights = new HashMap<>();
        Map<Long, Double> totalCommodityValues = new HashMap<>();
        Map<Long, Commodity> commoditiesById = new HashMap<>();

        List<Long> declarationIds = declarations.stream()
                .map(Declaration::getDeclarationId)
                .toList();

        packageItemRepo.findByDeclarationIdIn(declarationIds).forEach(packageItem -> {
            Commodity commodity = packageItem.getCommodity();
            commoditiesById.putIfAbsent(commodity.getCommodityId(), commodity);

            double weight = totalCommodityWeights.getOrDefault(commodity.getCommodityId(), 0d);
            totalCommodityWeights.put(commodity.getCommodityId(), weight + packageItem.getNetWeight());

            double value = totalCommodityValues.getOrDefault(commodity.getCommodityId(), 0d);
            totalCommodityValues.put(commodity.getCommodityId(), value + packageItem.getValue());
        });

        return commoditiesById.values().stream()
                .map(commodity -> {
                    double totalWeight = totalCommodityWeights.get(commodity.getCommodityId());
                    double totalValue = totalCommodityValues.get(commodity.getCommodityId());

                    return mapShipmentCommodityStats(commodity, totalWeight, totalValue);
                })
                .toList();
    }

    private ShipmentCommodityStats mapShipmentCommodityStats(Commodity commodity, Double totalWeight, Double totalValue) {
        return ShipmentCommodityStats.builder()
                .commodityId(commodity.getCommodityId())
                .commodityCode(commodity.getCommodityCode())
                .description(commodity.getDescription())
                .totalWeight(totalWeight)
                .totalValue(totalValue)
                .build();
    }

    private ShipmentDeclarationStats mapShipmentDeclarationStats(Declaration declaration) {
        return ShipmentDeclarationStats.builder()
                .declarationId(declaration.getDeclarationId())
                .identifierCode(declaration.getIdentifierCode())
                .totalWeight(declaration.getTotalWeight())
                .totalValue(declaration.getTotalValue())
                .build();
    }
}
