package lv.pakit.service.declaration;

import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.dto.response.DeclarationResponse;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.model.Declaration;
import lv.pakit.model.shipment.Shipment;
import lv.pakit.repo.IDeclarationRepo;
import lv.pakit.repo.IPackageItemRepo;
import lv.pakit.service.ClientService;
import lv.pakit.service.PackageItemService;
import lv.pakit.service.shipment.ShipmentService;
import lv.pakit.service.auth.AuthService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeclarationService {

    private final ClientService clientService;
    private final ShipmentService shipmentService;
    private final PackageItemService packageItemService;
    private final AuthService authService;
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
        Shipment shipment = declaration.getShipment();

        return DeclarationResponse.builder()
                .declarationId(declaration.getDeclarationId())
                .clientId(ofNullable(client).map(Client::getClientId).orElse(null))
                .clientFullName(ofNullable(client).map(Client::getFullName).orElse(null))
                .shipmentId(ofNullable(shipment).map(Shipment::getShipmentId).orElse(null))
                .shipmentCode(ofNullable(shipment).map(Shipment::getShipmentCode).orElse(null))
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
                .createdAt(declaration.getCreatedAt())
                .packageItems(packageItemService.fetchByDeclarationId(declaration.getDeclarationId()))
                .packageAmount(declaration.getPackageAmount())
                .build();
    }

    private Declaration.DeclarationBuilder mapFromDto(DeclarationRequest request) {

        String createdByFullName = authService.getAuthenticatedUser().getFirstName() + " " + authService.getAuthenticatedUser().getLastName();
        Client client = ofNullable(request.getClientId())
                .map(clientService::requireById)
                .orElse(null);
        Shipment shipment = ofNullable(request.getShipmentId())
                .map(shipmentService::requireById)
                .orElse(null);
        Long shipmentId = ofNullable(shipment)
                .map(Shipment::getShipmentId)
                .orElse(null);

        return Declaration.builder()
                .client(client)
                .shipmentId(shipmentId)
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
                .createdBy(createdByFullName)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .packageAmount(request.getPackageAmount());
    }

    public Declaration requireById(long id) {
        return declarationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Declaration with id (" + id + ") not found!"));
    }

    public List<Declaration> requireAllById(List<Long> ids) {
        List<Declaration> declarations = declarationRepo.findAllById(ids);

        if (declarations.size() != ids.size()) {
            throw new NotFoundException("Declarations with ids not found");
        }

        return declarations;
    }
}
