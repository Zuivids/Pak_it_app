package lv.pakit.services;

import lv.pakit.dto.request.packageItem.PackageItemRequest;
import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.dto.response.PackageItemResponse;
import lv.pakit.model.Commodity;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IPackageItemRepo;
import lv.pakit.service.CommodityService;
import lv.pakit.service.PackageItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PackageItemServiceTests {

    @Autowired
    private PackageItemService packageItemService;

    @MockitoBean
    private IPackageItemRepo packageItemRepo;

    @MockitoBean
    private CommodityService commodityService;

    @Test
    void fetchByDeclarationIdShouldReturnMappedResponse() {
        Commodity commodity = Commodity.builder()
                .commodityId(123L)
                .commodityCode("1111155555")
                .description("Table")
                .build();

        PackageItem item = PackageItem.builder()
                .packageItemId(321L)
                .commodity(commodity)
                .declarationId(333L)
                .quantity(5)
                .netWeight(10.22)
                .value(99.99)
                .used(true)
                .build();

        when(packageItemRepo.findByDeclarationId(333L)).thenReturn(List.of(item));
        when(commodityService.mapToDto(commodity)).thenReturn(
                CommodityResponse.builder()
                        .commodityId(123L)
                        .commodityCode("1111155555")
                        .description("Table")
                        .build()
        );

        List<PackageItemResponse> responses = packageItemService.fetchByDeclarationId(333L);

        assertEquals(1, responses.size());
        PackageItemResponse response = responses.get(0);

        assertEquals(321L, response.getPackageItemId());
        assertEquals(333L, response.getDeclarationId());
        assertEquals(5, response.getQuantity());
        assertEquals(10.22, response.getNetWeight());
        assertEquals(99.99, response.getValue());
        assertTrue(response.isUsed());
        assertEquals("1111155555", response.getCommodity().getCommodityCode());
        assertEquals("Table", response.getCommodity().getDescription());
    }

    @Test
    void createAllShouldCallSaveAllWithMappedEntities() {
        PackageItemRequest packageItem = new PackageItemRequest();
        packageItem.setCommodityId(101L);
        packageItem.setQuantity(10);
        packageItem.setNetWeight(2.5);
        packageItem.setValue(100.22);
        packageItem.setUsed(false);

        Commodity mockCommodity = Commodity.builder()
                .commodityId(101L)
                .commodityCode("9999911111")
                .description("Chair")
                .build();

        when(commodityService.requireById(101L)).thenReturn(mockCommodity);

        List<PackageItemRequest> requests = new ArrayList<>();
        requests.add(packageItem);

        packageItemService.createAll(321L, requests);

        verify(packageItemRepo, times(1)).saveAll(argThat(iterable -> {
            Iterator<PackageItem> iterator = iterable.iterator();
            if (!iterator.hasNext()) return false;

            PackageItem item = iterator.next();

            return !iterator.hasNext() &&
                    item.getDeclarationId() == 321L &&
                    item.getCommodity().equals(mockCommodity) &&
                    item.getQuantity() == 10 &&
                    item.getNetWeight() == 2.5 &&
                    item.getValue() == 100.22 &&
                    !item.isUsed();
        }));
    }

    @Test
    void calculateTotalWeightShouldSumCorrectly() {
        PackageItemRequest packageItem1 = new PackageItemRequest();
        packageItem1.setNetWeight(1.5);

        PackageItemRequest packageItem2 = new PackageItemRequest();
        packageItem2.setNetWeight(2.5);

        PackageItemRequest packageItem3 = new PackageItemRequest();
        packageItem3.setNetWeight(3.0);

        List<PackageItemRequest> requests = List.of(packageItem1, packageItem2, packageItem3);

        double totalWeight = packageItemService.calculateTotalWeight(requests);
        assertEquals(7.0, totalWeight);
    }

    @Test
    void calculateTotalValueShouldSumCorrectly() {
        PackageItemRequest packageItem1 = new PackageItemRequest();
        packageItem1.setValue(50.0);

        PackageItemRequest packageItem2 = new PackageItemRequest();
        packageItem2.setValue(75.5);

        PackageItemRequest packageItem3 = new PackageItemRequest();
        packageItem3.setValue(24.5);

        List<PackageItemRequest> requests = List.of(packageItem1, packageItem2, packageItem3);

        double totalValue = packageItemService.calculateTotalValue(requests);
        assertEquals(150.0, totalValue);
    }
}
