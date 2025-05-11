package lv.pakit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CommodityRequest {

    @NotBlank
    private String commodityCode;
    @NotBlank
    private String description;
}
