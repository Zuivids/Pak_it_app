package lv.pakit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommodityRequest {

    @NotBlank
    private String commodityCode;
    @NotBlank
    private String description;
}
