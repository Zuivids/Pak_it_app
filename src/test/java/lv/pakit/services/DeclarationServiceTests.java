package lv.pakit.services;

import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.dto.request.packageItem.PackageItemRequest;
import lv.pakit.dto.response.DeclarationResponse;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.model.Declaration;
import lv.pakit.repo.IDeclarationRepo;
import lv.pakit.service.ClientService;
import lv.pakit.service.DeclarationService;
import lv.pakit.service.PackageItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class DeclarationServiceTests {

    @Autowired
    private DeclarationService declarationService;
    @MockitoBean
    private PackageItemService packageItemService;

    @MockitoBean
    private IDeclarationRepo declarationRepo;

    @MockitoBean
    private ClientService clientService;

    @Test
    void fetchByIdShouldReturnMappedResponse() {
        Client client = Client.builder().clientId(123L).fullName("Tests Testeris").build();
        Declaration declaration = Declaration.builder()
                .declarationId(1L)
                .client(client)
                .identifierCode("MAY321")
                .senderName("Sender")
                .senderAddress("333 iela")
                .senderCountryCode("LV")
                .senderPhoneNumber("12345678")
                .receiverName("Receiver")
                .receiverAddress("222 St")
                .receiverCountryCode("UK")
                .receiverPhoneNumber("87654321")
                .totalWeight(1.1)
                .totalValue(2.2)
                .date("2025-05-27")
                .build();

        when(declarationRepo.findById(1L)).thenReturn(Optional.of(declaration));
        when(packageItemService.fetchByDeclarationId(1L)).thenReturn(Collections.emptyList());

        DeclarationResponse response = declarationService.fetchById(1L);

        assertEquals(1L, response.getDeclarationId());
        assertEquals("Tests Testeris", response.getClientFullName());
        assertEquals("MAY321", response.getIdentifierCode());
        assertEquals(0, response.getPackageItems().size());
    }

    @Test
    void fetchByIdShouldThrowWhenNotFound() {
        when(declarationRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> declarationService.fetchById(999L));
    }

    @Test
    void createShouldSaveDeclarationAndCreatePackageItems() {
        PackageItemRequest itemRequest = new PackageItemRequest();
        itemRequest.setCommodityId(1L);
        itemRequest.setQuantity(1);
        itemRequest.setNetWeight(1.0);
        itemRequest.setValue(100.0);
        itemRequest.setUsed(false);

        DeclarationRequest request = DeclarationRequest.builder()
                .clientId(1L)
                .identifierCode("MAY123")
                .senderName("Sender")
                .senderAddress("123 Iela")
                .senderCountryCode("LV")
                .senderPhoneNumber("12345678")
                .receiverName("Receiver")
                .receiverAddress("321 St")
                .receiverCountryCode("UK")
                .receiverPhoneNumber("87654321")
                .date("2025-05-27")
                .packageItems(List.of(itemRequest))
                .build();

        Client client = Client.builder().clientId(1L).build();

        when(clientService.requireById(1L)).thenReturn(client);
        when(packageItemService.calculateTotalWeight(any())).thenReturn(1.0);
        when(packageItemService.calculateTotalValue(any())).thenReturn(100.0);
        when(declarationRepo.save(any())).thenReturn(
                Declaration.builder().declarationId(123L).client(client).build()
        );

        declarationService.create(request);

        verify(declarationRepo).save(any(Declaration.class));
        verify(packageItemService).createAll(123L, request.getPackageItems());
    }

    @Test
    void updateByIdShouldOverwriteDeclaration() {
        long id = 99L;
        DeclarationRequest request = DeclarationRequest.builder()
                .clientId(1L)
                .identifierCode("DEF456")
                .senderName("Sender")
                .senderAddress("123 St")
                .senderCountryCode("LV")
                .senderPhoneNumber("12345678")
                .receiverName("Receiver")
                .receiverAddress("321 St")
                .receiverCountryCode("EE")
                .receiverPhoneNumber("87654321")
                .date("2024-01-01")
                .packageItems(Collections.emptyList())
                .build();

        when(declarationRepo.findById(id)).thenReturn(Optional.of(new Declaration()));
        when(clientService.requireById(1L)).thenReturn(Client.builder().clientId(1L).build());
        when(packageItemService.calculateTotalWeight(any())).thenReturn(0.0);
        when(packageItemService.calculateTotalValue(any())).thenReturn(0.0);

        declarationService.updateById(id, request);

        verify(declarationRepo).save(any(Declaration.class));
        verify(packageItemService).createAll(id, Collections.emptyList());
    }

    @Test
    void deleteByIdShouldInvokeRepoDelete() {
        declarationService.deleteById(777L);
        verify(declarationRepo).deleteById(777L);
    }

    @Test
    void searchShouldReturnMatchingResults() {
        DeclarationSearchRequest request = DeclarationSearchRequest.builder()
                .senderName("TestSender")
                .build();

        Declaration declaration = Declaration.builder()
                .declarationId(1L)
                .senderName("TestSender")
                .client(Client.builder().clientId(1L).fullName("Test Client").build())
                .build();

        when(declarationRepo.findAll(any(Specification.class)))
                .thenReturn(List.of(declaration));
        when(packageItemService.fetchByDeclarationId(1L)).thenReturn(Collections.emptyList());

        var result = declarationService.search(request);

        assertEquals(1, result.size());
        assertEquals("TestSender", result.get(0).getSenderName());
    }
}