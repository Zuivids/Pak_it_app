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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

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
                .totalWeight(roundToTwoDecimals(calculateTotalWeight(declarations)))
                .totalValue(roundToTwoDecimals(calculateTotalValue(declarations)))
                .totalPackageAmount(calculateTotalPackageAmount(declarations))
                .declarationStats(getDeclarationStats(declarations))
                .commodityStats(getCommodityStats(declarations))
                .build();
    }

    private double calculateTotalWeight(List<Declaration> declarations) {
        return declarations.stream()
                .mapToDouble(Declaration::getTotalWeight)
                .sum();
    }

    private double calculateTotalValue(List<Declaration> declarations) {
        return declarations.stream()
                .mapToDouble(Declaration::getTotalValue)
                .sum();
    }

    private long calculateTotalPackageAmount(List<Declaration> declarations) {
        return declarations.stream()
                .filter(declaration -> declaration.getPackageAmount() != null)
                .mapToLong(Declaration::getPackageAmount)
                .sum();
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
        Map<Long, Long> totalCommodityQuantities = new HashMap<>();

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

            long quantity = totalCommodityQuantities.getOrDefault(commodity.getCommodityId(), 0L);
            totalCommodityQuantities.put(commodity.getCommodityId(), quantity + packageItem.getQuantity());
        });

        return commoditiesById.values().stream()
                .map(commodity -> {
                    double totalWeight = totalCommodityWeights.get(commodity.getCommodityId());
                    double totalValue = totalCommodityValues.get(commodity.getCommodityId());
                    long quantity = totalCommodityQuantities.get(commodity.getCommodityId());

                    return mapShipmentCommodityStats(commodity, totalWeight, totalValue, quantity);
                })
                .toList();
    }

    private ShipmentCommodityStats mapShipmentCommodityStats(Commodity commodity, Double totalWeight, Double totalValue,
                                                             Long quantity) {
        return ShipmentCommodityStats.builder()
                .commodityId(commodity.getCommodityId())
                .commodityCode(commodity.getCommodityCode())
                .description(commodity.getDescription())
                .totalWeight(totalWeight)
                .totalValue(totalValue)
                .quantity(quantity)
                .build();
    }

    private ShipmentDeclarationStats mapShipmentDeclarationStats(Declaration declaration) {
        long packageAmount = ofNullable(declaration.getPackageAmount()).orElse(0L);

        return ShipmentDeclarationStats.builder()
                .declarationId(declaration.getDeclarationId())
                .identifierCode(declaration.getIdentifierCode())
                .totalWeight(declaration.getTotalWeight())
                .totalValue(declaration.getTotalValue())
                .packageAmount(packageAmount)
                .build();
    }

    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
