package lv.pakit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lv.pakit.model.Commodity;

@Builder
@Getter
@Setter
public class PackageItemRequest {

    @NotNull
    private Long commodityId;
    private Commodity commodity;
    private long declarationId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double netWeight;
    @NotNull
    private Double value;
    @NotNull
    private Boolean used;

}
