package lv.pakit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommodityDto {

    private long commodityId;
    private String commodityCode;
    private String description;
}
