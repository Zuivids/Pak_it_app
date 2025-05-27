package lv.pakit.controller.page;

import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.service.CommodityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommodityPageController.class)
@WithMockUser(username = "testuser", roles = "USER")
class CommodityPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommodityService commodityService;

    @Test
    void getAllCommoditiesWithoutQueryParam() throws Exception {
        when(commodityService.fetchByQuery(null)).thenReturn(List.of());

        mockMvc.perform(get("/commodity"))
                .andExpect(status().isOk())
                .andExpect(view().name("commodity-show-many-page"))
                .andExpect(model().attributeExists("commodities"));

        verify(commodityService).fetchByQuery(null);
    }

    @Test
    void getAllCommoditiesWithQueryParam() throws Exception {
        when(commodityService.fetchByQuery("test")).thenReturn(List.of());

        mockMvc.perform(get("/commodity").param("query", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("commodity-show-many-page"))
                .andExpect(model().attributeExists("commodities"));

        verify(commodityService).fetchByQuery("test");
    }

    @Test
    void showCommodityCreateForm() throws Exception {
        mockMvc.perform(get("/commodity/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("commodity-add-new-page"));
    }

    @Test
    void showCommodityEditForm() throws Exception {
        var mockCommodity = CommodityResponse.builder()
                .commodityId(123L)
                .commodityCode("ABC123")
                .description("Test Commodity")
                .build();

        when(commodityService.fetchById(123L)).thenReturn(mockCommodity);

        mockMvc.perform(get("/commodity/123/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("commodity-edit-page"))
                .andExpect(model().attributeExists("commodity"));

        verify(commodityService).fetchById(123L);
    }

    @Test
    void showCommodityDeleteForm() throws Exception {
        var mockCommodity = CommodityResponse.builder()
                .commodityId(321L)
                .commodityCode("9999911111")
                .description("Test Commodity")
                .build();

        when(commodityService.fetchById(321L)).thenReturn(mockCommodity);

        mockMvc.perform(get("/commodity/321/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("commodity-delete-page"))
                .andExpect(model().attributeExists("commodity"));

        verify(commodityService).fetchById(321L);
    }
}
