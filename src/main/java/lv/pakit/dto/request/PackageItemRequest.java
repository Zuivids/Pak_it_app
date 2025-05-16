package lv.pakit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lv.pakit.model.Commodity;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PackageItemRequest {

    @NotNull
    private Long commodityId;
    private Long declarationId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double netWeight;
    @NotNull
    private Double value;
    @NotNull
    private Boolean used;

}
