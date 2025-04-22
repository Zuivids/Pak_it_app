package lv.pakit.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {

    ELECTRONICS("Electronics"),
    TOOLS("Tools"),
    TOYS("Toys"),
    SPORTS_EQUIPMENT("Sports Equipment"),
    FOOD("Food"),
    BEVERAGES("Beverages"),
    ALCOHOL("Alcohol"),
    TOBACCO("Tobacco"),
    PHARMACEUTICALS("Pharmaceuticals"),
    COSMETICS("Cosmetics"),
    CURRENCY("Currency"),
    ANTIQUES("Antiques"),
    PRESSURIZED_CONTAINERS("Pressurized Containers");

    private final String publicName;

    public static ProductCategory fromName(String name) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.getPublicName().equals(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException();
    }

}
