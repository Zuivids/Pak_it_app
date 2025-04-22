package lv.pakit.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ProductFragility {
    FRAGILE("Fragile"),
    NON_FRAGILE("Not Fragile");

    private final String publicName;

    public static ProductFragility fromName(String name) {
        for (ProductFragility fragility : ProductFragility.values()) {
            if (fragility.getPublicName().equals(name)) {
                return fragility;
            }
        }
        throw new IllegalArgumentException();
    }
}
