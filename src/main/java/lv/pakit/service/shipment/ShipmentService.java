package lv.pakit.service.shipment;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.request.shipment.ShipmentCreateRequest;
import lv.pakit.dto.request.shipment.ShipmentSearchRequest;
import lv.pakit.dto.response.shipment.ShipmentResponse;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.Declaration;
import lv.pakit.model.user.User;
import lv.pakit.model.shipment.Shipment;
import lv.pakit.repo.IDeclarationRepo;
import lv.pakit.repo.IShipmentRepo;
import lv.pakit.service.auth.AuthService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static lv.pakit.utils.SpecificationUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {

    private final AuthService authService;
    private final IShipmentRepo shipmentRepo;
    private final IDeclarationRepo declarationRepo;

    @Transactional
    public List<ShipmentResponse> fetchAll() {
        return shipmentRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public void create(ShipmentCreateRequest request) {
        Shipment shipment = Shipment.builder()
                .shipmentCode(request.getShipmentCode())
                .createdAt(LocalDateTime.now())
                .createdBy(authService.getAuthenticatedUser())
                .deliveryDatetime(request.getDeliveryDatetime())
                .deliveryState(request.getDeliveryState())
                .build();

        shipmentRepo.save(shipment);
    }

    @Transactional
    public void updateById(long shipmentId, ShipmentCreateRequest request) {
        Shipment shipment = requireById(shipmentId);
        shipment.setShipmentCode(request.getShipmentCode());
        shipment.setDeliveryDatetime(request.getDeliveryDatetime());
        shipment.setDeliveryState(request.getDeliveryState());

        shipmentRepo.save(shipment);
    }

    @Transactional
    public void deleteById(long shipmentId) {
        List<Declaration> declarations = declarationRepo.findByShipmentId(shipmentId);
        declarations.forEach(declaration -> {
            declaration.setShipmentId(null);
            declaration.setShipment(null);
        });

        declarationRepo.saveAll(declarations);
        shipmentRepo.deleteById(shipmentId);
    }

    @Transactional
    public List<ShipmentResponse> search(ShipmentSearchRequest request) {
        return shipmentRepo.findAll(matchesSearchRequest(request)).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public ShipmentResponse findById(long id) {
        Shipment shipment = requireById(id);

        return mapToDto(shipment);
    }

    private Specification<Shipment> matchesSearchRequest(ShipmentSearchRequest request) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addValueLike(predicates, root, cb, "shipmentCode", request.getShipmentCode());
            addValueEqualTo(predicates, root, cb, "deliveryState", request.getDeliveryState());
            addDatetimeInDate(predicates, root, cb, "deliveryDatetime", request.getDeliveryDate());

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private ShipmentResponse mapToDto(Shipment shipment) {
        return ShipmentResponse.builder()
                .shipmentId(shipment.getShipmentId())
                .shipmentCode(shipment.getShipmentCode())
                .createdAt(shipment.getCreatedAt())
                .createdBy(ofNullable(shipment.getCreatedBy()).map(User::fullName).orElse(null))
                .deliveryDatetime(shipment.getDeliveryDatetime())
                .deliveryState(shipment.getDeliveryState())
                .build();
    }

    public Shipment requireById(long id) {
        return shipmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Shipment with id (" + id + ") not found!"));
    }
}
