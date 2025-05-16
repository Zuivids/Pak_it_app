package lv.pakit.services.tests;

import lv.pakit.dto.PackageItemDto;
import lv.pakit.dto.request.PackageItemRequest;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Commodity;
import lv.pakit.model.Declaration;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IPackageItemRepo;
import lv.pakit.service.CommodityService;
import lv.pakit.service.PackageItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PackageItemServiceTests {

    @Mock
    private IPackageItemRepo packageItemRepo;

    @Mock
    private CommodityService commodityService;

    @InjectMocks
    private PackageItemService packageItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void packageItemCreateTest() {
        PackageItemRequest request = new PackageItemRequest(123L, 321L, 10, 1.5D, 100D, false);

        packageItemService.create(request);

        verify(packageItemRepo, times(1)).save(any(PackageItem.class));
    }

    @Test
    void packageItemRetrieveById_ValidTest() {
        PackageItem packageItem = PackageItem.builder().packageItemId(111L).commodity(Commodity.builder().commodityId(222L).commodityCode("1234567891").description("Test Description").build()).declaration(Declaration.builder().declarationId(333L).build()).quantity(1).netWeight(20D).value(10.99D).used(true).build();

        when(packageItemRepo.findById(123L)).thenReturn(Optional.of(packageItem));

        PackageItemDto packageItemDto = packageItemService.retrieveById(123L);

        assertEquals(111L, packageItemDto.getPackageItemId());
        assertEquals(1, packageItemDto.getQuantity());
        assertEquals(20D, packageItemDto.getNetWeight());
        assertEquals(10.99D, packageItemDto.getValue());
        assertTrue(packageItemDto.isUsed());


        assertEquals(222L, packageItemDto.getCommodity().getCommodityId());
        assertEquals("1234567891", packageItemDto.getCommodity().getCommodityCode());
        assertEquals("Test Description", packageItemDto.getCommodity().getDescription());

        assertEquals(333L, packageItemDto.getDeclarationId()); //TODO fix this issue
    }

    @Test
    void packageItemRetrieveByID_InvalidTest() {
        when(packageItemRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> packageItemService.retrieveById(1L));
    }

    @Test
    void packageItemRetrieveAllTest() {
        List<PackageItem> packageItemList = List.of(PackageItem.builder().packageItemId(111L).commodity(Commodity.builder().commodityId(112L).commodityCode("1111567891").description("Test Description1").build()).declaration(Declaration.builder().declarationId(113L).build()).quantity(10).netWeight(10.10D).value(10.10D).used(false).build(),

                PackageItem.builder().packageItemId(212L).commodity(Commodity.builder().commodityId(213L).commodityCode("1222567891").description("Test Description2").build()).declaration(Declaration.builder().declarationId(213L).build()).quantity(20).netWeight(20.20D).value(20.20D).used(true).build(),

                PackageItem.builder().packageItemId(313L).commodity(Commodity.builder().commodityId(314L).commodityCode("1333567891").description("Test Description3").build()).declaration(Declaration.builder().declarationId(315L).build()).quantity(30).netWeight(30.30D).value(30.30D).used(false).build());

        when(packageItemRepo.findAll()).thenReturn(packageItemList);

        List<PackageItemDto> packageItemDtoList = packageItemService.retrieveAll();

        assertEquals(3, packageItemDtoList.size());

        assertEquals(111L, packageItemDtoList.get(0).getPackageItemId());
        assertEquals(112L,packageItemDtoList.get(0).getCommodity().getCommodityId());
        assertEquals("1111567891",packageItemDtoList.get(0).getCommodity().getCommodityCode());
        assertEquals("Test Description1",packageItemDtoList.get(0).getCommodity().getDescription());

        assertEquals(213L, packageItemDtoList.get(1).getDeclarationId());
        assertEquals(20, packageItemDtoList.get(1).getQuantity());

        assertEquals(30.30D, packageItemDtoList.get(2).getNetWeight());
        assertEquals(30.30D, packageItemDtoList.get(2).getValue());

    }

    @Test
    void packageItemUpdateByIdTest() {
        PackageItem packageItem = PackageItem.builder().packageItemId(111L).commodity(Commodity.builder().commodityId(222L).commodityCode("1234567891").description("Test Description").build()).declaration(Declaration.builder().declarationId(333L).build()).quantity(1).netWeight(20D).value(10.99D).used(true).build();
        //TODO add possibility to change commodityCode or Description
        PackageItemRequest request = new PackageItemRequest(222L, 333L, 99, 100.99D, 2000.99D, false);

        when(packageItemRepo.findById(111L)).thenReturn(Optional.of(packageItem));

        packageItemService.updateById(111L, request);

        //TODO add commodity and declaration verification
        assertEquals(99, packageItem.getQuantity());
        assertEquals(100.99D, packageItem.getNetWeight());
        assertEquals(2000.99D, packageItem.getValue());
        assertFalse(packageItem.isUsed());
    }

    @Test
    void packageItemDeleteByIdTest() {
        PackageItem packageItem = PackageItem.builder().packageItemId(111L).build();
        when(packageItemRepo.findById(111L)).thenReturn(Optional.of(packageItem));

        packageItemService.deleteById(111L);

        verify(packageItemRepo).deleteById(111L);
    }

    @Test
    void packageItemSearchNullTest() {
        //TODO
    }

    @Test
    void packageItemSearchTest() {
        //TODO
    }

}
