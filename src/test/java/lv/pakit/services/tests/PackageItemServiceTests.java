//package lv.pakit.services.tests;
//
//import lv.pakit.dto.PackageItemDto;
//import lv.pakit.exception.NotFoundException;
//import lv.pakit.model.Commodity;
//import lv.pakit.model.Declaration;
//import lv.pakit.model.PackageItem;
//import lv.pakit.repo.IPackageItemRepo;
//import lv.pakit.service.PackageItemService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class PackageItemServiceTests {
//
//    @Autowired
//    private PackageItemService packageItemService;
//
//    @MockitoBean
//    private IPackageItemRepo packageItemRepo;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void packageItemCreateTest() {
//        PackageItemDto packageItemDto = PackageItemDto.builder()
//                .packageItemId(123L)
//                .declarationId(321L)
//                .quantity(10)
//                .netWeight(1.5D)
//                .value(100D)
//                .used(false)
//                .build();
//
//        packageItemService.create(packageItemDto);
//
//        verify(packageItemRepo, times(1)).save(any(PackageItem.class));
//    }
//
//    @Test
//    void packageItemRetrieveById_ValidTest() {
//        PackageItem packageItem = PackageItem.builder()
//                .packageItemId(111L)
//                .commodity(Commodity.builder()
//                        .commodityId(222L)
//                        .commodityCode("1234567891")
//                        .description("Test Description")
//                        .build())
//                .declaration(Declaration.builder()
//                        .declarationId(333L)
//                        .build())
//                .quantity(1)
//                .netWeight(20D)
//                .value(10.99D)
//                .used(true)
//                .build();
//
//        when(packageItemRepo.findById(111L)).thenReturn(Optional.of(packageItem));
//
//        PackageItemDto packageItemDto = packageItemService.retrieveById(111L);
//        System.out.println("===" + packageItemDto.getCommodity());
//        assertEquals(111L, packageItemDto.getPackageItemId());
//        assertEquals(1, packageItemDto.getQuantity());
//        assertEquals(20D, packageItemDto.getNetWeight());
//        assertEquals(10.99D, packageItemDto.getValue());
//        assertTrue(packageItemDto.isUsed());
//
//        assertEquals(222L, packageItemDto.getCommodity().getCommodityId());
//        assertEquals("1234567891", packageItemDto.getCommodity().getCommodityCode());
//        assertEquals("Test Description", packageItemDto.getCommodity().getDescription());
//
//        assertEquals(333L, packageItemDto.getDeclarationId());
//    }
//
//    @Test
//    void packageItemRetrieveByID_InvalidTest() {
//        when(packageItemRepo.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> packageItemService.retrieveById(1L));
//    }
//
//    @Test
//    void packageItemRetrieveAllTest() {
//        List<PackageItem> packageItemList = List.of(
//                PackageItem.builder()
//                        .packageItemId(111L)
//                        .commodity(
//                                Commodity.builder()
//                                        .commodityId(112L)
//                                        .commodityCode("1111567891")
//                                        .description("Test Description1")
//                                        .build()
//                        )
//                        .declaration(Declaration.builder().declarationId(113L).build())
//                        .quantity(10)
//                        .netWeight(10.10D)
//                        .value(10.10D)
//                        .used(false)
//                        .build(),
//
//                PackageItem.builder()
//                        .packageItemId(212L)
//                        .commodity(
//                                Commodity.builder()
//                                        .commodityId(213L)
//                                        .commodityCode("1222567891")
//                                        .description("Test Description2")
//                                        .build()
//                        )
//                        .declaration(Declaration.builder().declarationId(213L).build())
//                        .quantity(20)
//                        .netWeight(20.20D)
//                        .value(20.20D)
//                        .used(true)
//                        .build(),
//
//                PackageItem.builder()
//                        .packageItemId(313L)
//                        .commodity(
//                                Commodity.builder()
//                                        .commodityId(314L)
//                                        .commodityCode("1333567891")
//                                        .description("Test Description3")
//                                        .build()
//                        ).declaration(Declaration.builder().declarationId(315L).build())
//                        .quantity(30)
//                        .netWeight(30.30D)
//                        .value(30.30D)
//                        .used(false).build());
//
//        when(packageItemRepo.findAll()).thenReturn(packageItemList);
//
//        List<PackageItemDto> packageItemDtoList = packageItemService.retrieveAll();
//
//        assertEquals(3, packageItemDtoList.size());
//
//        assertEquals(111L, packageItemDtoList.get(0).getPackageItemId());
//        assertEquals(112L, packageItemDtoList.get(0).getCommodity().getCommodityId());
//        assertEquals("1111567891", packageItemDtoList.get(0).getCommodity().getCommodityCode());
//        assertEquals("Test Description1", packageItemDtoList.get(0).getCommodity().getDescription());
//
//        assertEquals(213L, packageItemDtoList.get(1).getDeclarationId());
//        assertEquals(20, packageItemDtoList.get(1).getQuantity());
//
//        assertEquals(30.30D, packageItemDtoList.get(2).getNetWeight());
//        assertEquals(30.30D, packageItemDtoList.get(2).getValue());
//
//    }
//
//    @Test
//    void packageItemUpdateByIdTest() {
//        PackageItem packageItem = PackageItem.builder()
//                .packageItemId(111L)
//                .commodity(
//                        Commodity.builder()
//                                .commodityId(222L)
//                                .commodityCode("1234567891")
//                                .description("Test Description")
//                                .build()
//                )
//                .declaration(Declaration.builder().declarationId(333L).build())
//                .quantity(1)
//                .netWeight(20D)
//                .value(10.99D)
//                .used(true)
//                .build();
//        //TODO add possibility to change commodityCode or Description
//        PackageItemDto packageItemDto = PackageItemDto.builder()
//                .packageItemId(111L)
//                .commodity(CommodityDto.builder()
//                        .commodityId(222L)
//                        .commodityCode("UPDATED-CODE")
//                        .description("Updated Description")
//                        .build())
//                .declarationId(333L)
//                .quantity(99)
//                .netWeight(100.99D)
//                .value(2000.99D)
//                .used(false)
//                .build();
//        when(packageItemRepo.findById(111L)).thenReturn(Optional.of(packageItem));
//
//        packageItemService.updateById(111L, packageItemDto);
//
//        //TODO add commodity and declaration verification
//        assertEquals(99, packageItem.getQuantity());
//        assertEquals(100.99D, packageItem.getNetWeight());
//        assertEquals(2000.99D, packageItem.getValue());
//        assertFalse(packageItem.isUsed());
//    }
//
//    @Test
//    void packageItemDeleteByIdTest() {
//        PackageItem packageItem = PackageItem.builder().packageItemId(111L).build();
//        when(packageItemRepo.findById(111L)).thenReturn(Optional.of(packageItem));
//
//        packageItemService.deleteById(111L);
//
//        verify(packageItemRepo).deleteById(111L);
//    }
//
//    @Test
//    void packageItemSearchNullTest() {
//        //TODO
//    }
//
//    @Test
//    void packageItemSearchTest() {
//        //TODO
//    }
//
//}
