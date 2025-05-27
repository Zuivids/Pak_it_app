package lv.pakit.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "declarationId")
public class DeclarationResponse {

    private long declarationId;
    private List<PackageItemResponse> packageItems;
    private long clientId;
    private String clientFullName;
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
}
