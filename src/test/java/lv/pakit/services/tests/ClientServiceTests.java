package lv.pakit.services.tests;

import lv.pakit.dto.ClientDto;
import lv.pakit.dto.request.ClientUpdateRequest;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import lv.pakit.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientServiceTests {

    @Mock
    private IClientRepo clientRepo;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void clientCreateTest() {
        Client client = new Client(
                123L,
                "testeris",
                "testStrongPassword",
                "testuser@gmail.com",
                "26451111",
                "Test Testeris"
        );

        clientRepo.save(client);

        verify(clientRepo, times(1)).save(any(Client.class));
    }

    @Test
    void clientRetrieveById_ValidTest() {
        Client client = Client.builder()
                .clientId(123L)
                .username("testeris")
                .password("StrongPassword1!")
                .email("testuser@gmail.com")
                .phoneNumber("26451111")
                .fullName("Test Testeris")
                .build();

        when(clientRepo.findById(123L)).thenReturn(Optional.of(client));

        ClientDto dto = clientService.retrieveById(123L);

        assertEquals(123L, dto.getClientId());
        assertEquals("testeris", dto.getUsername());
        assertEquals("StrongPassword1!", dto.getPassword());
        assertEquals("testuser@gmail.com", dto.getEmail());
        assertEquals("26451111", dto.getPhoneNumber());
        assertEquals("Test Testeris", dto.getFullName());
    }

    @Test
    void clientRetrieveById_InvalidTest() {
        Long invalidClientId = 999L;
        when(clientRepo.findById(invalidClientId)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(
                NotFoundException.class,
                () -> clientService.retrieveById(invalidClientId)
        );

        assertEquals("Client with id (999) not found!", thrown.getMessage());
    }

    @Test
    void clientRetrieveAllTest() {
        List<Client> clients = List.of(
                Client.builder()
                        .clientId(123L)
                        .username("Jānis")
                        .password("VeryStrongPassword1!")
                        .email("janisuser@gmail.com")
                        .phoneNumber("26451112")
                        .fullName("Jānis Testeris")
                        .build(),
                Client.builder()
                        .clientId(1234L)
                        .username("Pēteris")
                        .password("VeryStrongPassword2!")
                        .email("peterisuser@gmail.com")
                        .phoneNumber("26451113")
                        .fullName("Pēteris Testeris")
                        .build()
        );

        when(clientRepo.findAll()).thenReturn(clients);

        List<ClientDto> clientDtoList = clientService.retrieveAll();

        assertEquals(2, clientDtoList.size());
        assertEquals("Jānis Testeris", clientDtoList.get(0).getFullName());
        assertEquals("26451113", clientDtoList.get(1).getPhoneNumber());
    }

    @Test
    void clientUpdateById_ValidTest() {
        Client client = Client.builder()
                .clientId(12345L)
                .username("Miķelis")
                .password("VeryStrongPassword3!")
                .email("mikelisuser@gmail.com")
                .phoneNumber("26451114")
                .fullName("Miķelis Testeris")
                .build();

        ClientUpdateRequest request = new ClientUpdateRequest("mikelis@gmail.com", "12345678", "Jānis Testeris");

        when(clientRepo.findById(12345L)).thenReturn(Optional.of(client));

        clientService.updateById(12345L, request);

        ClientDto dto = clientService.retrieveById(12345L);

        assertEquals("mikelis@gmail.com", dto.getEmail());
        assertEquals("12345678", dto.getPhoneNumber());
    }

    @Test
    void clientDeleteByIdTest() {
        Client client = Client.builder().clientId(123L).build();
        when(clientRepo.findById(123L)).thenReturn(Optional.of(client));

        clientService.deleteById(123L);

        verify(clientRepo).deleteById(123L);
    }

    //TODO add search
}
