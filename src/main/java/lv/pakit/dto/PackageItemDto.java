package lv.pakit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
public class PackageItemDto {

    private Integer  id;
    @NotNull
    private int commodityId;
    private int declarationId;
    @NotNull
    @Min(1)
    private int quantity;
    @NotNull
    private double netWeight;
    @NotNull
    private double value;
    @NotNull
    private boolean used;

}
