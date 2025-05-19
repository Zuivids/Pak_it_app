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

        if (request.getIdentifierCode() != null && !request.getIdentifierCode().isBlank()) {
            return declarationRepo.findByIdentifierCodeContainingIgnoreCase(request.getIdentifierCode())
                    .stream().map(this::mapToDto).toList();
        }

        //TODO clients name

        if (request.getSenderName() != null && !request.getSenderName().isBlank()) {
            return declarationRepo.findBySenderNameContainingIgnoreCase(request.getSenderName())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getSenderAddress() != null && !request.getSenderAddress().isBlank()) {
            return declarationRepo.findBySenderAddressContainingIgnoreCase(request.getSenderAddress())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getSenderCountryCode() != null && !request.getSenderCountryCode().isBlank()) {
            return declarationRepo.findBySenderCountryCodeContainingIgnoreCase(request.getSenderCountryCode())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getSenderPhoneNumber() != null && !request.getSenderPhoneNumber().isBlank()) {
            return declarationRepo.findBySenderPhoneNumberContainingIgnoreCase(request.getSenderPhoneNumber())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getReceiverName() != null && !request.getReceiverName().isBlank()) {
            return declarationRepo.findByReceiverNameContainingIgnoreCase(request.getReceiverName())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getReceiverAddress() != null && !request.getReceiverAddress().isBlank()) {
            return declarationRepo.findByReceiverAddressContainingIgnoreCase(request.getReceiverAddress())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getReceiverCountryCode() != null && !request.getReceiverCountryCode().isBlank()) {
            return declarationRepo.findByReceiverCountryCodeContainingIgnoreCase(request.getReceiverCountryCode())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getReceiverPhoneNumber() != null && !request.getReceiverPhoneNumber().isBlank()) {
            return declarationRepo.findByReceiverPhoneNumberContainingIgnoreCase(request.getReceiverPhoneNumber())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getTotalWeight() != null && !request.getTotalWeight().isNaN()) {
            return declarationRepo.findByTotalWeight(request.getTotalWeight())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getTotalValue() != null && !request.getTotalValue().isNaN()) {
            return declarationRepo.findByTotalValue(request.getTotalValue())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getDate() != null && !request.getDate().isBlank()) {
            return declarationRepo.findByDateContainingIgnoreCase(request.getDate())
                    .stream().map(this::mapToDto).toList();
        }

        return declarationRepo.findAll().stream().map(this::mapToDto).toList();
    }
}
