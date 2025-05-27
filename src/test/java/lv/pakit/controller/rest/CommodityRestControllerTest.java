package lv.pakit.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.service.CommodityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommodityRestController.class)
@WithMockUser(username = "testuser", roles = "USER")
class CommodityRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommodityService commodityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveCommodityShouldReturn200() throws Exception {
        var request = new CommodityRequest();
        request.setCommodityCode("1111177777");
        request.setDescription("Used Books");

        mockMvc.perform(post("/commodity")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(commodityService).create(Mockito.refEq(request));
    }

    @Test
    void editCommodityShouldReturn200() throws Exception {
        var request = new CommodityRequest();
        request.setCommodityCode("4444411111");
        request.setDescription("New magazines");

        mockMvc.perform(put("/commodity/44")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(commodityService).updateById(Mockito.eq(44L), Mockito.refEq(request));
    }

    @Test
    void deleteCommodityShouldReturn200() throws Exception {
        mockMvc.perform(delete("/commodity/7/delete")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(commodityService).deleteById(7L);
    }
}
