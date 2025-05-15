package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.DeclarationDto;
import lv.pakit.dto.request.DeclarationRequest;
import lv.pakit.dto.request.DeclarationSearchRequest;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Declaration;
import lv.pakit.repo.IDeclarationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final IDeclarationRepo declarationRepo;
    private final ClientService clientService;

    public void create(DeclarationRequest declarationRequest) {
        Declaration declaration = mapToDeclaration(declarationRequest);
        declarationRepo.save(declaration);
    }

    public DeclarationDto retriveById(long id) {
        Declaration declaration = requireDeclarationById(id);

        return mapToDto(declaration);
    }

    public List<DeclarationDto> retriveAll() {
        return declarationRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void updateById(long id, DeclarationDto dto) {
        Declaration declaration = requireDeclarationById(id);

        declaration.setIdentifierCode(dto.getIdentifierCode());
        declaration.setSenderName(dto.getSenderName());
        declaration.setSenderAddress(dto.getSenderAddress());
        declaration.setSenderPhoneNumber(dto.getSenderPhoneNumber());
        declaration.setSenderCountryCode(dto.getSenderCountryCode());
        declaration.setReceiverName(dto.getReceiverName());
        declaration.setReceiverAddress(dto.getReceiverAddress());
        declaration.setReceiverCountryCode(dto.getReceiverCountryCode());
        declaration.setReceiverPhoneNumber(dto.getReceiverPhoneNumber());
        declaration.setTotalWeight(dto.getTotalWeight());
        declaration.setTotalValue(dto.getTotalValue());
        declaration.setDate(dto.getDate());

        declarationRepo.save(declaration);
    }

    public void deleteById(long id) {
        requireDeclarationById(id);
        declarationRepo.deleteById(id);
    }

    public DeclarationDto mapToDto(Declaration declaration) {

        return DeclarationDto.builder()
                .declarationId(declaration.getDeclarationId())
                .client(clientService.mapToDto(declaration.getClient()))
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
                .build();
    }

    public Declaration mapToDeclaration(DeclarationRequest declarationRequest) {
        return Declaration.builder()
                .identifierCode(declarationRequest.getIdentifier_code())
                .senderName(declarationRequest.getSenderName())
                .senderAddress(declarationRequest.getSenderAddress())
                .senderCountryCode(declarationRequest.getSenderCountryCode())
                .senderPhoneNumber(declarationRequest.getSenderPhoneNumber())
                .receiverName(declarationRequest.getReceiverName())
                .receiverAddress(declarationRequest.getReceiverAddress())
                .receiverCountryCode(declarationRequest.getReceiverCountryCode())
                .receiverPhoneNumber(declarationRequest.getReceiverPhoneNumber())
                .totalWeight(declarationRequest.getTotalWeight())
                .totalValue(declarationRequest.getTotalValue())
                .date(declarationRequest.getDate())
                .build();
    }

    private Declaration requireDeclarationById(long id) {
        return declarationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Declaration with id (" + id + ") not found!"));
    }

    public List<DeclarationDto> search(DeclarationSearchRequest request) {
        if (request.getIdentifierCode() == null || request.getIdentifierCode().isEmpty()) {
            return declarationRepo.findAll().stream()
                    .map(this::mapToDto)
                    .toList();
        }

        List<Declaration> declarations = declarationRepo.findByIdentifierCodeContainingIgnoreCase(request.getIdentifierCode());
        return declarations.stream()
                .map(this::mapToDto)
                .toList();
    }
}
