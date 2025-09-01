package lv.pakit.services;

import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.exception.http.FieldErrorException;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.Commodity;
import lv.pakit.repo.ICommodityRepo;
import lv.pakit.service.CommodityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommodityServiceTests {

    @Autowired
    private CommodityService commodityService;

    @MockitoBean
    private ICommodityRepo commodityRepo;

    private CommodityRequest buildRequest(String code, String desc) {
        CommodityRequest request = new CommodityRequest();
        request.setCommodityCode(code);
        request.setDescription(desc);
        return request;
    }

    private Commodity buildCommodity(Long id, String code, String desc) {
        return Commodity.builder()
                .commodityId(id)
                .commodityCode(code)
                .description(desc)
                .build();
    }

    @Test
    void createShouldSaveNewCommodity() {
        CommodityRequest request = buildRequest("1234567891", "Table");

        when(commodityRepo.findByCommodityCode("1234567891")).thenReturn(Optional.empty());

        commodityService.create(request);

        verify(commodityRepo).save(any(Commodity.class));
    }

    @Test
    void createShouldThrowWhenDuplicateCode() {
        CommodityRequest request = buildRequest("1234567891", "Chair");

        when(commodityRepo.findByCommodityCode("1234567891"))
                .thenReturn(Optional.of(buildCommodity(1L, "1234567891", "Table")));

        assertThrows(FieldErrorException.class, () -> commodityService.create(request));
    }

    @Test
    void updateShouldUpdateAndSave() {
        Commodity existing = buildCommodity(123L, "1111155555", "Old monitor");
        CommodityRequest updated = buildRequest("5555511111", "New monitor");

        when(commodityRepo.findById(123L)).thenReturn(Optional.of(existing));
        when(commodityRepo.findByCommodityCode("5555511111")).thenReturn(Optional.empty());

        commodityService.updateById(123L, updated);

        verify(commodityRepo).save(existing);
        assertEquals("5555511111", existing.getCommodityCode());
        assertEquals("New monitor", existing.getDescription());
    }

    @Test
    void updateShouldSkipCheckIfCodeUnchanged() {
        Commodity existing = buildCommodity(1234L, "1987654321", "Old keyboard");
        CommodityRequest updated = buildRequest("1987654321", "Updated keyboard");

        when(commodityRepo.findById(1234L)).thenReturn(Optional.of(existing));

        commodityService.updateById(1234L, updated);

        verify(commodityRepo).save(existing);
        verify(commodityRepo, never()).findByCommodityCode(anyString());
        assertEquals("Updated keyboard", existing.getDescription());
    }

    @Test
    void deleteShouldInvokeRepoDelete() {
        commodityService.deleteById(777L);
        verify(commodityRepo).deleteById(777L);
    }

    @Test
    void fetchByIdShouldReturnMappedDto() {
        Commodity commodity = buildCommodity(444L, "3333322222", "Rock");

        when(commodityRepo.findById(444L)).thenReturn(Optional.of(commodity));

        CommodityResponse result = commodityService.fetchById(444L);

        assertEquals("3333322222", result.getCommodityCode());
        assertEquals("Rock", result.getDescription());
        assertEquals(444L, result.getCommodityId());
    }

    @Test
    void fetchByIdShouldThrowIfNotFound() {
        when(commodityRepo.findById(404L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commodityService.fetchById(404L));
    }

    @Test
    void fetchByQueryShouldSearchWithQuery() {
        String query = "fragile";
        List<Commodity> result = List.of(buildCommodity(666L, "6666666666", "Fragile Glass"));

        when(commodityRepo.findByCommodityCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query))
                .thenReturn(result);

        List<CommodityResponse> response = commodityService.fetchByQuery(query);

        assertEquals(1, response.size());
        assertEquals("6666666666", response.get(0).getCommodityCode());
    }

    @Test
    void fetchByQueryShouldReturnAllWhenQueryEmpty() {
        List<Commodity> all = List.of(
                buildCommodity(888L, "8888811111", "Mobile Charger"),
                buildCommodity(777L, "7777711111", "Earphones Charger"),
                buildCommodity(555L, "5555511111", "Laptop Charger"));
        when(commodityRepo.findAll()).thenReturn(all);

        List<CommodityResponse> response = commodityService.fetchByQuery("");

        assertEquals(3, response.size());
    }

    @Test
    void fetchByQueryShouldReturnAllWhenQueryIsNull() {
        List<Commodity> all = List.of(
                buildCommodity(888L, "8888811111", "Mobile Charger"),
                buildCommodity(777L, "7777711111", "Earphones Charger"),
                buildCommodity(555L, "5555511111", "Laptop Charger"));
        when(commodityRepo.findAll()).thenReturn(all);

        List<CommodityResponse> response = commodityService.fetchByQuery(null);

        assertEquals(3, response.size());
    }

    @Test
    void fetchAllShouldReturnAllMapped() {
        List<Commodity> all = List.of(
                buildCommodity(123L, "1212121212", "Boots"),
                buildCommodity(321L, "2323232323", "Used Boots")
        );

        when(commodityRepo.findAll()).thenReturn(all);

        List<CommodityResponse> result = commodityService.fetchAll();

        assertEquals(2, result.size());
        assertEquals("1212121212", result.get(0).getCommodityCode());
    }

    @Test
    void mapToDtoShouldCorrectlyMapFields() {
        Commodity commodity = buildCommodity(999L, "9999999999", "Used Boots");
        CommodityResponse response = commodityService.mapToDto(commodity);

        assertEquals(999L, response.getCommodityId());
        assertEquals("9999999999", response.getCommodityCode());
        assertEquals("Used Boots", response.getDescription());
    }
}
