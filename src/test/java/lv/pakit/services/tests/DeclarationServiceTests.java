package lv.pakit.services.tests;

import lv.pakit.dto.ClientDto;
import lv.pakit.dto.DeclarationDto;
import lv.pakit.dto.request.DeclarationRequest;
import lv.pakit.dto.request.DeclarationSearchRequest;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.model.Declaration;
import lv.pakit.repo.IDeclarationRepo;
import lv.pakit.service.ClientService;
import lv.pakit.service.DeclarationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DeclarationServiceTests {

    @Autowired
    private DeclarationService declarationService;

    @MockitoBean
    private IDeclarationRepo declarationRepo;

    @MockitoBean
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void declarationCreateTest() {
        DeclarationRequest request = DeclarationRequest.builder()
                .identifier_code("MAR25")
                .senderName("Sender")
                .senderAddress("Sender Street")
                .senderPhoneNumber("LV")
                .senderCountryCode("12345678")
                .receiverName("Receiver")
                .receiverAddress("Receiver Street")
                .receiverPhoneNumber("UK")
                .receiverCountryCode("87654321")
                .totalWeight(12.5)
                .totalValue(99.99)
                .date(LocalDate.now().toString())
                .build();

        declarationService.create(request);

        verify(declarationRepo, times(1)).save(any(Declaration.class));
    }

    @Test
    void declarationRetrieveById_ValidTest() {
        Declaration declaration = Declaration.builder()
                .declarationId(1L)
                .client(Client.builder().clientId(10L).build())
                .identifierCode("MAR25")
                .senderName("Sender")
                .senderAddress("Sender Street")
                .senderPhoneNumber("LV")
                .senderCountryCode("12345678")
                .receiverName("Receiver")
                .receiverAddress("Receiver Street")
                .receiverPhoneNumber("UK")
                .receiverCountryCode("87654321")
                .totalWeight(12.5D)
                .totalValue(99.99D)
                .date(LocalDate.now().toString())
                .build();

        when(declarationRepo.findById(1L)).thenReturn(Optional.of(declaration));
        when(clientService.mapToDto(any())).thenReturn(ClientDto.builder().clientId(10L).build());

        DeclarationDto dto = declarationService.retriveById(1L);

        assertEquals("MAR25", dto.getIdentifierCode());
        assertEquals("Receiver", dto.getReceiverName());
        assertEquals(10L, dto.getClient().getClientId());
    }

    @Test
    void declarationRetrieveById_InvalidTest() {
        when(declarationRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> declarationService.retriveById(99L));
    }

    @Test
    void declarationRetrieveAllTest() {
        List<Declaration> declarations = List.of(
                Declaration.builder().declarationId(1L).identifierCode("APR25").client(new Client()).build(),
                Declaration.builder().declarationId(2L).identifierCode("MAR25").client(new Client()).build()
        );

        when(declarationRepo.findAll()).thenReturn(declarations);
        when(clientService.mapToDto(any())).thenReturn(ClientDto.builder().build());

        List<DeclarationDto> list = declarationService.retriveAll();

        assertEquals(2, list.size());
    }

    @Test
    void declarationUpdateById_ValidTest() {
        Declaration existing = Declaration.builder()
                .declarationId(1L)
                .identifierCode("OLD")
                .build();

        DeclarationDto updated = DeclarationDto.builder()
                .identifierCode("NEW")
                .senderName("Sender")
                .senderAddress("Sender Address")
                .senderPhoneNumber("123")
                .senderCountryCode("UK")
                .receiverName("Receiver")
                .receiverAddress("Receiver  Address")
                .receiverCountryCode("LV")
                .receiverPhoneNumber("456")
                .totalWeight(10.0)
                .totalValue(200.0)
                .date(LocalDate.now().toString())
                .build();

        when(declarationRepo.findById(1L)).thenReturn(Optional.of(existing));

        declarationService.updateById(1L, updated);

        assertEquals("NEW", existing.getIdentifierCode());
        assertEquals("Sender", existing.getSenderName());
        assertEquals("Receiver", existing.getReceiverName());
        verify(declarationRepo).save(existing);
    }

    @Test
    void declarationDeleteById_ValidTest() {
        Declaration declaration = Declaration.builder().declarationId(1L).build();
        when(declarationRepo.findById(1L)).thenReturn(Optional.of(declaration));

        declarationService.deleteById(1L);

        verify(declarationRepo).deleteById(1L);
    }

    @Test
    void declarationSearch_NullIdentifierCodeTest() {
        List<Declaration> declarations = List.of(
                Declaration.builder().declarationId(1L).identifierCode("MAY25").client(new Client()).build()
        );
        when(declarationRepo.findAll()).thenReturn(declarations);
        when(clientService.mapToDto(any())).thenReturn(ClientDto.builder().build());

        List<DeclarationDto> list = declarationService.search(DeclarationSearchRequest.builder().build());

        assertEquals(1, list.size());
    }

    @Test
    void declarationSearch_ByIdentifierCodeTest() {
        List<Declaration> declarations = List.of(
                Declaration.builder().declarationId(1L).identifierCode("JUN25").client(new Client()).build(),
                Declaration.builder().declarationId(2L).identifierCode("JUN19").client(new Client()).build()
        );

        when(declarationRepo.findByIdentifierCodeContainingIgnoreCase("JUN")).thenReturn(declarations);
        when(clientService.mapToDto(any())).thenReturn(ClientDto.builder().build());

        DeclarationSearchRequest request = DeclarationSearchRequest.builder().identifierCode("JUN").build();

        List<DeclarationDto> result = declarationService.search(request);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getIdentifierCode().startsWith("JUN")));
    }

}
