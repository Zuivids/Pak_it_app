package lv.pakit.model.shipment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lv.pakit.model.Classifier;

@Getter
@RequiredArgsConstructor
public enum DeliveryState implements Classifier {

    PLANNED("Planned"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    private final String publicName;
}
