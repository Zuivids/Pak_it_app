package lv.pakit.dto.request.declaration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lv.pakit.dto.request.packageitem.PackageItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationRequest {

    @NotNull
    private Long clientId;
    @NotEmpty
    @Valid
    private List<PackageItemRequest> packageItems;
    @NotBlank
    private String identifierCode;
    @NotBlank
    private String senderName;
    @NotBlank
    private String senderAddress;
    @NotBlank
    private String senderCountryCode;
    @NotBlank
    private String senderPhoneNumber;
    @NotBlank
    private String receiverName;
    @NotBlank
    private String receiverAddress;
    @NotBlank
    private String receiverCountryCode;
    @NotBlank
    private String receiverPhoneNumber;
    @NotNull
    private String date;

    private String createdBy;
    private LocalDateTime createdAt;
}