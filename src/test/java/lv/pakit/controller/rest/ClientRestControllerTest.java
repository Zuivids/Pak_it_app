package lv.pakit.controller.rest;

import lv.pakit.dto.request.client.ClientCreateRequest;
import lv.pakit.dto.request.client.ClientUpdateRequest;
import lv.pakit.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClientRestController.class)
@WithMockUser(username = "testuser", roles = "USER")
class ClientRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveClientShouldReturn200() throws Exception {
        var request = new ClientCreateRequest();
        request.setUsername("testuser");
        request.setPassword("Pass123!");
        request.setEmail("testuser@example.com");
        request.setPhoneNumber("+37211113333");
        request.setFullName("John Doe");

        mockMvc.perform(post("/client")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(clientService).create(Mockito.any(ClientCreateRequest.class));
    }

    @Test
    void saveClientShouldReturn400WhenInvalid() throws Exception {
        var request = new ClientCreateRequest();

        mockMvc.perform(post("/client")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editClientShouldReturn200() throws Exception {
        var request = new ClientUpdateRequest();
        request.setFullName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPhoneNumber("+37211113333");

        mockMvc.perform(put("/client/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(clientService).updateById(Mockito.eq(1L), Mockito.any(ClientUpdateRequest.class));
    }

    @Test
    void editClientShouldReturn400WhenInvalid() throws Exception {
        var request = new ClientUpdateRequest();

        mockMvc.perform(put("/client/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteClientShouldReturn200() throws Exception {
        mockMvc.perform(delete("/client/1/delete")
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(clientService).deleteById(1L);
    }

    @Test
    void deleteClientShouldReturn400WhenInvalidId() throws Exception {
        mockMvc.perform(delete("/client/invalid-id/delete")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}