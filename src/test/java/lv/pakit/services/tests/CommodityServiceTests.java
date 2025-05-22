//package lv.pakit.services.tests;
//
//import lv.pakit.exception.NotFoundException;
//import lv.pakit.model.Commodity;
//import lv.pakit.repo.ICommodityRepo;
//import lv.pakit.service.CommodityService;
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
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class CommodityServiceTests {
//
//    @Autowired
//    private CommodityService commodityService;
//    @MockitoBean
//    private ICommodityRepo commodityRepo;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void commodityCreateTest() {
//        CommodityDto commodityDto = new CommodityDto(1L,"12345678", "Test Commodity");
//
//        commodityService.create(commodityDto);
//
//        verify(commodityRepo, times(1)).save(any(Commodity.class));
//    }
//
//    @Test
//    void commodityRetrieveById_ValidTest() {
//        Commodity commodity = Commodity.builder()
//                .commodityId(123L)
//                .commodityCode("87654321")
//                .description("Test commodity")
//                .build();
//
//        when(commodityRepo.findById(123L)).thenReturn(Optional.of(commodity));
//
//        CommodityDto dto = commodityService.retrieveById(123L);
//
//        assertEquals("87654321", dto.getCommodityCode());
//        assertEquals("Test commodity", dto.getDescription());
//    }
//
//    @Test
//    void commodityRetrieveByID_InvalidTest() {
//        when(commodityRepo.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> commodityService.retrieveById(1L));
//    }
//
//    @Test
//    void commodityRetrieveAllTest() {
//        List<Commodity> commodities = List.of(
//                Commodity.builder().commodityId(123L).commodityCode("1234567891").description("Glue").build(),
//                Commodity.builder().commodityId(1234L).commodityCode("1234567892").description("Table").build(),
//                Commodity.builder().commodityId(12345L).commodityCode("1234567893").description("Light bulb").build());
//
//        when(commodityRepo.findAll()).thenReturn(commodities);
//
//        List<CommodityDto> commodityDtoList = commodityService.retrieveAll();
//
//        assertEquals(3, commodityDtoList.size());
//        assertEquals("Table", commodityDtoList.get(1).getDescription());
//        assertEquals("1234567893", commodityDtoList.get(2).getCommodityCode());
//    }
//
//    @Test
//    void commodityUpdateById_ValidTest() {
//        Commodity commodity = Commodity.builder()
//                .commodityId(123L)
//                .commodityCode("12345678")
//                .description("Keyboard")
//                .build();
//
//        CommodityDto commodityDto = CommodityDto.builder()
//                .commodityId(123L)
//                .commodityCode("87654321")
//                .description("Monitor")
//                .build();
//
//        when(commodityRepo.findById(123L)).thenReturn(Optional.of(commodity));
//
//        commodityService.updateById(123L, commodityDto);
//
//        assertEquals("87654321", commodity.getCommodityCode());
//        assertEquals("Monitor", commodity.getDescription());
//        verify(commodityRepo).save(commodity);
//    }
//
//    @Test
//    void commodityDeleteByIdTest() {
//        Commodity commodity = Commodity.builder().commodityId(123L).build();
//        when(commodityRepo.findById(1L)).thenReturn(Optional.of(commodity));
//
//        commodityService.deleteById(1L);
//
//        verify(commodityRepo).deleteById(1L);
//    }
//
//    @Test
//    void commoditySearchNullTest() {
//        List<Commodity> commodities = List.of(Commodity.builder().commodityId(123L).commodityCode("1234567894").description("Closet").build());
//        when(commodityRepo.findAll()).thenReturn(commodities);
//
//        List<CommodityDto> commodityDtoList = commodityService.search(null);
//
//        assertEquals(1,commodities.size());
//    }
//
//    @Test
//    void commodityCommodityCodeSearchTest () {
//        List<Commodity> commodities = List.of(
//                Commodity.builder().commodityId(123L).commodityCode("1234567895").description("Spoon").build(),
//                Commodity.builder().commodityId(1234L).commodityCode("1234567896").description("Fork").build(),
//                Commodity.builder().commodityId(12345L).commodityCode("1234567897").description("Knife").build()
//        );
//        when(commodityRepo.findByCommodityCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase("1234567896","1234567896")).thenReturn(commodities);
//
//        List<CommodityDto> commodityDtoList = commodityService.search("1234567896");
//
//        assertEquals(3,commodities.size());
//        assertEquals("1234567896", commodityDtoList.get(1).getCommodityCode());
//    }
//
//    @Test
//    void commodityDescriptionSearchTest () {
//        List<Commodity> commodities = List.of(
//                Commodity.builder().commodityId(123L).commodityCode("3234567895").description("Sock").build(),
//                Commodity.builder().commodityId(12345L).commodityCode("3234567897").description("Sandal").build()
//        );
//        when(commodityRepo.findByCommodityCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Sandal","Sandal")).thenReturn(commodities);
//
//        List<CommodityDto> commodityDtoList = commodityService.search("Sandal");
//
//        assertEquals(2,commodities.size());
//        assertEquals("Sandal", commodityDtoList.get(1).getDescription());
//    }
//
//}
