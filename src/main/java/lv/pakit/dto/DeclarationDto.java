package lv.pakit.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationDto {

    private long declarationId;
    private ClientDto client; // TODO add option to choose from
    private List<CommodityDto> commodityDtoList = new ArrayList<>();
    private List<PackageItemDto> packageItemDtoList;
    private String identifierCode;
    private String senderName;
    private String senderAddress;
    private String senderCountryCode;
    private String senderPhoneNumber;
    private String receiverName;
    private String receiverAddress;
    private String receiverCountryCode;
    private String receiverPhoneNumber;
    private double totalWeight;
    private double totalValue;
    private String date;

}
