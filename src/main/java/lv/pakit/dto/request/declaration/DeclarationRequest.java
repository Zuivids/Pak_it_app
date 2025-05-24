package lv.pakit.dto.request.declaration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lv.pakit.dto.request.packageItem.PackageItemRequest;

import java.util.List;

@Getter
@Setter
@Builder
public class DeclarationRequest {

    private long clientId;
    private List<PackageItemRequest> packageItems;
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
