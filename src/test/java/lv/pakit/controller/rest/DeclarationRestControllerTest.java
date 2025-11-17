package lv.pakit.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.dto.request.packageitem.PackageItemRequest;
import lv.pakit.service.DeclarationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(DeclarationRestController.class)
@WithMockUser(username = "testuser", roles = "USER")
class DeclarationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeclarationService declarationService;

    @Autowired
    private ObjectMapper objectMapper;

    private DeclarationRequest buildValidRequest() {
        var item = new PackageItemRequest();
        item.setCommodityId(123L);
        item.setQuantity(10);
        item.setNetWeight(5.5);
        item.setValue(100.0);
        item.setUsed(false);

        return DeclarationRequest.builder()
                .clientId(1L)
                .packageItems(List.of(item))
                .identifierCode("MAY2025")
                .senderName("Janis Testeris")
                .senderAddress("Brivibas iela 23")
                .senderCountryCode("LV")
                .senderPhoneNumber("+3711111111")
                .receiverName("Valdis Testeris")
                .receiverAddress("Test Street 33")
                .receiverCountryCode("UK")
                .receiverPhoneNumber("+3722222222")
                .date("2025-05-29")
                .build();
    }

    @Test
    void saveDeclarationShouldReturn200() throws Exception {
        var request = buildValidRequest();

        mockMvc.perform(post("/declaration")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        Mockito.verify(declarationService).create(Mockito.any(DeclarationRequest.class));
    }

    @Test
    void saveDeclarationShouldReturn400WhenInvalid() throws Exception {
        var request = new DeclarationRequest();

        mockMvc.perform(post("/declaration")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDeclarationShouldReturn200() throws Exception {
        var request = buildValidRequest();

        mockMvc.perform(put("/declaration/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(declarationService).updateById(Mockito.eq(5L), Mockito.any(DeclarationRequest.class));
    }

    @Test
    void updateDeclarationShouldReturn400WhenInvalid() throws Exception {
        var request = new DeclarationRequest();

        mockMvc.perform(put("/declaration/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteDeclarationShouldReturn200() throws Exception {
        mockMvc.perform(delete("/declaration/7")
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(declarationService).deleteById(7L);
    }

    @Test
    void deleteDeclarationShouldReturn400WhenInvalidId() throws Exception {
        mockMvc.perform(delete("/declaration/invalid-id")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
