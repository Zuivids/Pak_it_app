package lv.pakit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
public class DeclarationRequest {

    @NotNull
    private String identifier_code;
    @NotNull
    private String senderName;
    @NotNull
    private String senderAddress;
    @NotNull
    private String senderCountryCode;
    @NotNull
    private String senderPhoneNumber;
    @NotNull
    private String receiverName;
    @NotNull
    private String receiverAddress;
    @NotNull
    private String receiverCountryCode;
    @NotNull
    private String receiverPhoneNumber;
    private double totalWeight;
    private double totalValue;
    private String date;
}
