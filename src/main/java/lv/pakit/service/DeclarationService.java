package lv.pakit.service;

import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.dto.response.DeclarationResponse;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.model.Declaration;
import lv.pakit.repo.IDeclarationRepo;
import lv.pakit.repo.IPackageItemRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final ClientService clientService;
    private final PackageItemService packageItemService;
    private final IDeclarationRepo declarationRepo;
    private final IPackageItemRepo packageItemRepo;

    public DeclarationResponse fetchById(long declarationId) {
        Declaration declaration = requireById(declarationId);

        return mapToDto(declaration);
    }

    public List<DeclarationResponse> search(DeclarationSearchRequest request) {
        return declarationRepo.findAll(matchesSearchRequest(request)).stream()
                .map(this::mapToDto)
                .toList();
    }

    private Specification<Declaration> matchesSearchRequest(DeclarationSearchRequest request) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addValueLike(predicates, root, cb, "identifierCode", request.getIdentifierCode());
            addValueLike(predicates, root, cb, "senderName", request.getSenderName());
            addValueLike(predicates, root, cb, "senderAddress", request.getSenderAddress());
            addValueLike(predicates, root, cb, "senderCountryCode", request.getSenderCountryCode());
            addValueLike(predicates, root, cb, "senderPhoneNumber", request.getSenderPhoneNumber());
            addValueLike(predicates, root, cb, "receiverName", request.getReceiverName());
            addValueLike(predicates, root, cb, "receiverAddress", request.getReceiverAddress());
            addValueLike(predicates, root, cb, "receiverCountryCode", request.getReceiverCountryCode());
            addValueLike(predicates, root, cb, "receiverPhoneNumber", request.getReceiverPhoneNumber());
            addValueEqualTo(predicates, root, cb, "totalWeight", request.getTotalWeight());
            addValueEqualTo(predicates, root, cb, "totalValue", request.getTotalValue());
            addValueLike(predicates, root, cb, "date", request.getDate());
            addClientNameLike(predicates, root, cb, request);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addClientNameLike(List<Predicate> predicates, Root<Declaration> root, CriteriaBuilder cb, DeclarationSearchRequest request) {
        if (request.getClientName() != null && !request.getClientName().isEmpty()) {
            Join<Declaration, Client> declarationClient = root.join("client");
            predicates.add(cb.like(declarationClient.get("fullName"), "%" + request.getClientName() + "%"));
        }
    }

    private void addValueEqualTo(List<Predicate> predicates, Root<Declaration> root, CriteriaBuilder cb, String field, Object value) {
        if (value != null) {
            predicates.add(cb.equal(root.get(field), value));
        }
    }

    private void addValueLike(List<Predicate> predicates, Root<Declaration> root, CriteriaBuilder cb, String field, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.like(root.get(field), "%" + value + "%"));
        }
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

    @Transactional
    public void deleteById(long declarationId) {
        packageItemRepo.deleteByDeclarationId(declarationId);
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
                .createdBy(declaration.getCreatedBy())
                .createdDateTime(declaration.getCreatedDateTime())
                .packageItems(packageItemService.fetchByDeclarationId(declaration.getDeclarationId()))
                .build();
    }

    private Declaration.DeclarationBuilder mapFromDto(DeclarationRequest request) {
        String username = getCurrentUsername();

        return Declaration.builder()
                .client(clientService.requireById(request.getClientId()))
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
                .totalValue(packageItemService.calculateTotalValue(request.getPackageItems()))
                .createdBy(username)
                .createdDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : "SYSTEM";
    }

    public Declaration requireById(long id) {
        return declarationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Declaration with id (" + id + ") not found!"));
    }
}
