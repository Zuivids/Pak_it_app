package lv.pakit.services;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lv.pakit.dto.request.client.ClientCreateRequest;
import lv.pakit.dto.request.client.ClientUpdateRequest;
import lv.pakit.dto.response.ClientResponse;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import lv.pakit.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClientServiceTests {

    @Autowired
    private ClientService clientService;

    @MockitoBean
    private IClientRepo clientRepo;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private Validator validator;

    @BeforeEach
    void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Client buildClient(Long id) {
        return Client.builder()
                .clientId(id)
                .username("testeris")
                .password("StrongPassword1!")
                .email("testeris@inbox.lv")
                .phoneNumber("+37129292929")
                .fullName("Tests Testeris")
                .build();
    }

    @Test
    void fetchByIdShouldReturnClientResponse() {
        Client mockClient = buildClient(123L);
        when(clientRepo.findById(123L)).thenReturn(Optional.of(mockClient));

        ClientResponse result = clientService.fetchById(123L);

        assertEquals("testeris", result.getUsername());
        assertEquals("testeris@inbox.lv", result.getEmail());
    }

    @Test
    void fetchAllShouldReturnListOfClientResponses() {
        List<Client> clients = List.of(buildClient(123L), buildClient(234L));
        when(clientRepo.findAll()).thenReturn(clients);

        List<ClientResponse> result = clientService.fetchAll();

        assertEquals(2, result.size());
        assertEquals("testeris", result.get(0).getUsername());
    }

    @Test
    void clientCreateShouldSaveNewClient() {
        ClientCreateRequest request = new ClientCreateRequest();
        request.setUsername("jaunais");
        request.setPassword("VeryStrongPassword1!");
        request.setEmail("istais@inbox.lv");
        request.setPhoneNumber("+37129292999");
        request.setFullName("Jaunais Testeris");

        Client dummySavedClient = new Client();
        when(passwordEncoder.encode("VeryStrongPassword1!")).thenReturn("encodedPassword123");
        when(clientRepo.save(any(Client.class))).thenReturn(dummySavedClient);

        clientService.create(request);

        verify(passwordEncoder, times(1)).encode("VeryStrongPassword1!");
        verify(clientRepo, times(1)).save(any(Client.class));
    }

    @Test
    void clientCreateShouldFailValidationOnInvalidFields() {
        ClientCreateRequest request = new ClientCreateRequest();
        request.setUsername("??");
        request.setPassword("neder");
        request.setEmail("nav-epasts");
        request.setPhoneNumber("abcd");
        request.setFullName("12345");

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation to fail for invalid input");
        assertEquals(7, violations.size());
    }

    @Test
    void updateByIDShouldUpdateAndSaveClient() {
        Client existing = buildClient(123L);
        when(clientRepo.findById(123L)).thenReturn(Optional.of(existing));

        ClientUpdateRequest update = new ClientUpdateRequest();
        update.setEmail("neinbox@gmail.com");
        update.setPhoneNumber("+37129292777");
        update.setFullName("Uzlabotais Testeris");

        clientService.updateById(123L, update);

        assertEquals("neinbox@gmail.com", existing.getEmail());
        verify(clientRepo).save(existing);
    }

    @Test
    void deleteByIdShouldCallDelete() {
        clientService.deleteById(4321L);
        verify(clientRepo).deleteById(4321L);
    }

    @Test
    void mapToDtoShouldCorrectlyMapFields() {
        Client client = buildClient(777L);
        ClientResponse dto = clientService.mapToDto(client);

        assertEquals(client.getClientId(), dto.getClientId());
        assertEquals(client.getUsername(), dto.getUsername());
        assertEquals(client.getPassword(), dto.getPassword());
        assertEquals(client.getEmail(), dto.getEmail());
        assertEquals(client.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(client.getFullName(), dto.getFullName());
    }

    @Test
    void requireByIdShouldThrowWhenNotFound() {
        when(clientRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.requireById(99L));
    }

}
