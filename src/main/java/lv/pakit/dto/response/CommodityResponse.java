package lv.pakit.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "commodityId")
public class CommodityResponse {

    private long commodityId;
    private String commodityCode;
    private String description;
}