package lv.pakit.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeclarationSearchRequest {

    private String identifierCode;
    private String clientName;
    private String senderName;
    private String senderAddress;
    private String senderCountryCode;
    private String senderPhoneNumber;
    private String receiverName;
    private String receiverAddress;
    private String receiverCountryCode;
    private String receiverPhoneNumber;
    private Double totalWeight;
    private Double totalValue;
    private String date;
}
