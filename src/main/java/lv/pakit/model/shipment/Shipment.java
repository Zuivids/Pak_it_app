package lv.pakit.model.shipment;

import jakarta.persistence.*;
import lombok.*;
import lv.pakit.model.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table(name = "shipment")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private long shipmentId;
    private String shipmentCode;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    private LocalDateTime deliveryDatetime;
    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;
}
