package lv.pakit.controller.page;

import lv.pakit.dto.response.ClientResponse;
import lv.pakit.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientPageController.class)
@WithMockUser(username = "testuser", roles = "USER")
class ClientPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Test
    void getAllClientsShouldReturnClientListView() throws Exception {
        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(view().name("client-show-many-page"));

        Mockito.verify(clientService).fetchAll();
    }

    @Test
    void showClientFormShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/client/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("client-add-new-page"));
    }

    @Test
    void editClientShouldReturnEditPage() throws Exception {
        var dummyClient = ClientResponse.builder()
                .clientId(321L)
                .email("testeris@inbox.lv")
                .phoneNumber("+37211113333")
                .fullName("Tests Testeris")
                .build();

        Mockito.when(clientService.fetchById(321L)).thenReturn(dummyClient);

        mockMvc.perform(get("/client/321/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("client-edit-page"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    void deleteClientShouldReturnDeletePage() throws Exception {
        var mockClient = ClientResponse.builder()
                .clientId(123L)
                .email("navinbox@inbox.com")
                .phoneNumber("+372111122222")
                .fullName("Janis Testeris")
                .build();

        Mockito.when(clientService.fetchById(123L)).thenReturn(mockClient);

        mockMvc.perform(get("/client/123/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("client-delete-page"))
                .andExpect(model().attributeExists("client"));
    }
}
