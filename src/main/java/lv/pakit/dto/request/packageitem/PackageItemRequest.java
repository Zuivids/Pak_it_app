package lv.pakit.dto.request.packageitem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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