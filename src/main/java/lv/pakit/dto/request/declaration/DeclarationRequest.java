package lv.pakit.dto.request.declaration;

import lombok.Getter;
import lombok.Setter;
import lv.pakit.dto.request.packageItem.PackageItemRequest;
import lv.pakit.model.Client;

import java.util.List;

@Getter
@Setter
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
