package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.dto.request.packageItem.PackageItemRequest;
import lv.pakit.dto.response.ClientResponse;
import lv.pakit.dto.response.DeclarationResponse;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.model.Commodity;
import lv.pakit.model.Declaration;
import lv.pakit.model.PackageItem;
import lv.pakit.repo.IDeclarationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final IDeclarationRepo declarationRepo;
    private final CommodityService commodityService;
    private final ClientService clientService;
    private final PackageItemService packageItemService;

//    public DeclarationResponse defaultDeclaration(){
//
//        DeclarationResponse declarationDto = DeclarationResponse.builder()
//                .date(LocalDate.now().toString())
//                .identifierCode("%s %s".formatted(LocalDate.now().getMonth(), LocalDate.now().getYear()))
//                .totalValue(0)
//                .totalWeight(0)
//                .build();
//
//        return declarationDto;
//    }

    public void create(DeclarationRequest request) {

        Client client = clientService.requireById(request.getClientId());

        Declaration declaration = Declaration.builder()
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
                .totalWeight(request.getTotalWeight())
                .totalValue(request.getTotalValue())
                .date(request.getDate())
                .build();

        declarationRepo.save(declaration);

        List<PackageItem> packageItems = new ArrayList<>();
        for (PackageItemRequest itemRequest : request.getPackageItems()) {
            Commodity commodity = commodityService.requireById(itemRequest.getCommodity().getCommodityId());

            PackageItem item = PackageItem.builder()
                    .declaration(declaration)
                    .commodity(commodity)
                    .quantity(itemRequest.getQuantity())
                    .netWeight(itemRequest.getNetWeight())
                    .value(itemRequest.getValue())
                    .used(itemRequest.isUsed())
                    .build();

            packageItems.add(item);
        }

        packageItemService.saveAll(packageItems);
    }

//    public void updateById(long id, DeclarationRequest request) {
//        Declaration declaration = requireDeclarationById(id);
//
//        //TODO check if same declaration does not exist
//
//        declaration.setClient(request.getClient());
//        declaration.setIdentifierCode(request.getIdentifierCode());
//        declaration.setSenderName(request.getSenderName());
//        declaration.setSenderAddress(request.getSenderAddress());
//        declaration.setSenderCountryCode(request.getSenderCountryCode());
//        declaration.setSenderPhoneNumber(request.getSenderPhoneNumber());
//        declaration.setReceiverName(request.getReceiverName());
//        declaration.setReceiverAddress(request.getReceiverAddress());
//        declaration.setReceiverCountryCode(request.getReceiverCountryCode());
//        declaration.setReceiverPhoneNumber(request.getReceiverPhoneNumber());
//        declaration.setTotalWeight(request.getTotalWeight());
//        declaration.setTotalValue(request.getTotalValue());
//        declaration.setDate(request.getDate());
//
//        declarationRepo.save(declaration);
//    }

    public DeclarationResponse fetchById(long id) {
        Declaration declaration = requireDeclarationById(id);

        return mapToDto(declaration);
    }

    public List<DeclarationResponse> retriveAll() {
        return declarationRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteById(long id) {
        requireDeclarationById(id);
        declarationRepo.deleteById(id);
    }

    public DeclarationResponse mapToDto(Declaration declaration) {
        ClientResponse client = Optional.ofNullable(declaration.getClient())
                .map(clientService::mapToDto)
                .orElse(null);


        return DeclarationResponse.builder()
                .declarationId(declaration.getDeclarationId())
                .client(client)
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

    private Declaration requireDeclarationById(long id) {
        return declarationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Declaration with id (" + id + ") not found!"));
    }

    public List<DeclarationResponse> search(DeclarationSearchRequest request) {
        if (request.getIdentifierCode() != null && !request.getIdentifierCode().isBlank()) {
            return declarationRepo.findByIdentifierCodeContainingIgnoreCase(request.getIdentifierCode())
                    .stream().map(this::mapToDto).toList();
        }

        if (request.getClientName() != null && !request.getClientName().isBlank()) {
            return declarationRepo.findByClientFullNameContainingIgnoreCase(request.getClientName())
                    .stream().map(this::mapToDto).toList();
        }

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
