package lv.pakit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "commodityId")
public class CommodityDto {

    private long commodityId;
    @NotBlank(message = "Commodity code cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Commodity code must be exactly 10 digits")
    private String commodityCode;
    @NotBlank(message = "Description cannot be blank")
    @Pattern(regexp = ".*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*", message = "Description must contain at least 3 letters")
    private String description;
}
