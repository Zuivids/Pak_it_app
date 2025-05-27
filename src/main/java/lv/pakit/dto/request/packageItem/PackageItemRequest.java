package lv.pakit.dto.request.packageItem;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageItemRequest {

    @NotNull
    private Long commodityId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double netWeight;
    @NotNull
    private Double value;
    private boolean used;
}