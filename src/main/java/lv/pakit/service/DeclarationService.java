package lv.pakit.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.dto.response.DeclarationResponse;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.model.Declaration;
import lv.pakit.repo.IDeclarationRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final ClientService clientService;
    private final PackageItemService packageItemService;
    private final IDeclarationRepo declarationRepo;

    public DeclarationResponse fetchById(long declarationId) {
        Declaration declaration = requireById(declarationId);

        return mapToDto(declaration);
    }

    public List<DeclarationResponse> search(DeclarationSearchRequest request) {
        Declaration declarationExample = mapExampleDeclaration(request);
        ExampleMatcher exampleMatcher = mapExampleMatcher(request);

        return declarationRepo.findAll(Example.of(declarationExample, exampleMatcher)).stream()
                .map(this::mapToDto)
                .toList();
    }

    private Declaration mapExampleDeclaration(DeclarationSearchRequest request) {
        Declaration declarationExample = Declaration.builder()
                .identifierCode(blankToNull(request.getIdentifierCode()))
                .senderName(blankToNull(request.getSenderName()))
                .senderAddress(blankToNull(request.getSenderAddress()))
                .senderCountryCode(blankToNull(request.getSenderCountryCode()))
                .senderPhoneNumber(blankToNull(request.getSenderPhoneNumber()))
                .receiverName(blankToNull(request.getReceiverName()))
                .receiverAddress(blankToNull(request.getReceiverAddress()))
                .receiverCountryCode(blankToNull(request.getReceiverCountryCode()))
                .receiverPhoneNumber(blankToNull(request.getReceiverPhoneNumber()))
                .totalWeight(Optional.ofNullable(request.getTotalWeight()).orElse(0d))
                .totalValue(Optional.ofNullable(request.getTotalValue()).orElse(0d))
                .build();

        return declarationExample;
    }

    private ExampleMatcher mapExampleMatcher(DeclarationSearchRequest request) {
        List<String> ignoredPaths = new ArrayList<>(List.of("declarationId", "clientId"));

        if (request.getTotalWeight() == null) {
            ignoredPaths.add("totalWeight");
        }
        if (request.getTotalValue() == null) {
            ignoredPaths.add("totalValue");
        }

        return ExampleMatcher.matching()
                .withIgnorePaths(ignoredPaths.toArray(String[]::new));
    }

    @Transactional
    public void create(DeclarationRequest request) {
        Declaration declaration = declarationRepo.save(mapFromDto(request).build());

        packageItemService.createAll(declaration.getDeclarationId(), request.getPackageItems());
    }

    @Transactional
    public void updateById(long declarationId, DeclarationRequest request) {
        requireById(declarationId);
        Declaration declaration = mapFromDto(request)
                .declarationId(declarationId)
                .build();

        declarationRepo.save(declaration);
        packageItemService.createAll(declarationId, request.getPackageItems());
    }

    public void deleteById(long declarationId) {
        declarationRepo.deleteById(declarationId);
    }

    public DeclarationResponse mapToDto(Declaration declaration) {
        Client client = declaration.getClient();

        return DeclarationResponse.builder()
                .declarationId(declaration.getDeclarationId())
                .clientId(client.getClientId())
                .clientFullName(client.getFullName())
                .identifierCode(declaration.getIdentifierCode())
                .senderName(declaration.getSenderName())
                .senderAddress(declaration.getSenderAddress())
                .senderCountryCode(declaration.getSenderCountryCode())
                .senderPhoneNumber(declaration.getSenderPhoneNumber())
                .receiverName(declaration.getReceiverName())
                .receiverAddress(declaration.getReceiverAddress())
                .receiverCountryCode(declaration.getReceiverCountryCode())
                .receiverPhoneNumber(declaration.getReceiverPhoneNumber())
                .totalWeight(declaration.getTotalWeight())
                .totalValue(declaration.getTotalValue())
                .date(declaration.getDate())
                .packageItems(packageItemService.fetchByDeclarationId(declaration.getDeclarationId()))
                .build();
    }

    private Declaration.DeclarationBuilder mapFromDto(DeclarationRequest request) {
        Client client = clientService.requireById(request.getClientId());

        return Declaration.builder()
                .client(client)
                .identifierCode(request.getIdentifierCode())
                .senderName(request.getSenderName())
                .senderAddress(request.getSenderAddress())
                .senderCountryCode(request.getSenderCountryCode())
                .senderPhoneNumber(request.getSenderPhoneNumber())
                .receiverName(request.getReceiverName())
                .receiverAddress(request.getReceiverAddress())
                .receiverCountryCode(request.getReceiverCountryCode())
                .receiverPhoneNumber(request.getReceiverPhoneNumber())
                .date(request.getDate())
                .totalWeight(packageItemService.calculateTotalWeight(request.getPackageItems()))
                .totalValue(packageItemService.calculateTotalValue(request.getPackageItems()));
    }

    public Declaration requireById(long id) {
        return declarationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Declaration with id (" + id + ") not found!"));
    }

    private String blankToNull(String value) {
        return StringUtils.hasLength(value) ? value : null;
    }
}