package lv.pakit.model;

import jakarta.persistence.*;
import lombok.*;
import lv.pakit.model.shipment.Shipment;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table(name = "declaration")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private long declarationId;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "shipment_id", insertable = false, updatable = false)
    private Shipment shipment;
    @Column(name = "shipment_id")
    private Long shipmentId;
    private String identifierCode;
    private String senderName;
    private String senderAddress;
    private String senderCountryCode;
    private String senderPhoneNumber;
    private String receiverName;
    private String receiverAddress;
    private String receiverCountryCode;
    private String receiverPhoneNumber;
    private double totalWeight;
    private double totalValue;
    private String date;
    private String createdBy;
    private LocalDateTime createdAt;
    private Long packageAmount;
}
