package lv.pakit.dto.request.packageItem;

import lombok.Getter;
import lombok.Setter;
import lv.pakit.model.Commodity;
import lv.pakit.model.Declaration;

@Getter
@Setter
public class PackageItemRequest {

    private Commodity commodity;
    private Declaration declarationId;
    private int quantity;
    private double netWeight;
    private double value;
    private boolean used;
}
