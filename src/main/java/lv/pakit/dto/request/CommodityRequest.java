package lv.pakit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CommodityRequest {
    @NotNull
    private String commodityCode;
    @NotNull
    private String description;
}
