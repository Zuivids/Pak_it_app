package lv.pakit.controller.page;

import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.dto.response.DeclarationResponse;
import lv.pakit.service.ClientService;
import lv.pakit.service.CommodityService;
import lv.pakit.service.declaration.DeclarationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeclarationPageController.class)
@WithMockUser(username = "testuser", roles = "USER")
class DeclarationPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeclarationService declarationService;

    @MockitoBean
    private CommodityService commodityService;

    @MockitoBean
    private ClientService clientService;

    @Test
    void getAllDeclarationsShouldReturnListPage() throws Exception {
        when(declarationService.search(any(DeclarationSearchRequest.class))).thenReturn(List.of());

        mockMvc.perform(get("/declaration"))
                .andExpect(status().isOk())
                .andExpect(view().name("declaration-show-many-page"))
                .andExpect(model().attributeExists("declarations"));
    }

    @Test
    void getDeclarationByIdShouldReturnSingleDeclarationPage() throws Exception {
        var mockDeclaration = DeclarationResponse.builder()
                .declarationId(1001L)
                .build();

        when(declarationService.fetchById(1001L)).thenReturn(mockDeclaration);

        mockMvc.perform(get("/declaration/1001"))
                .andExpect(status().isOk())
                .andExpect(view().name("declaration-show-one-page"))
                .andExpect(model().attributeExists("declaration"));
    }

    @Test
    void showDeclarationCreateFormShouldReturnFormPageWithCommoditiesAndClients() throws Exception {
        when(commodityService.fetchAll()).thenReturn(List.of());
        when(clientService.fetchAll()).thenReturn(List.of());

        mockMvc.perform(get("/declaration/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("declaration-add-new-page"))
                .andExpect(model().attributeExists("commodities"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void showDeclarationEditFormShouldReturnEditPageWithData() throws Exception {
        var mockDeclaration = DeclarationResponse.builder()
                .declarationId(999L)
                .build();

        when(declarationService.fetchById(999L)).thenReturn(mockDeclaration);
        when(commodityService.fetchAll()).thenReturn(List.of());
        when(clientService.fetchAll()).thenReturn(List.of());

        mockMvc.perform(get("/declaration/999/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("declaration-edit-page"))
                .andExpect(model().attributeExists("declaration"))
                .andExpect(model().attributeExists("commodities"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void showDeclarationDeleteFormShouldReturnDeletePageWithDeclaration() throws Exception {
        var mockDeclaration = DeclarationResponse.builder()
                .declarationId(888L)
                .build();

        when(declarationService.fetchById(888L)).thenReturn(mockDeclaration);

        mockMvc.perform(get("/declaration/888/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("declaration-delete-page"))
                .andExpect(model().attributeExists("declaration"));
    }
}
