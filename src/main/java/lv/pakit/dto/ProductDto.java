package lv.pakit.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDto {

    private final int id;

    @NotBlank
    @Size(min = 3, max = 500)
    private final String title;

    @NotNull
    @Max(1000)
    @Min(1)
    private final int quantity;

    @NotBlank
    @Size(min = 3, max = 500)
    private final String description;

    @Enumerated(EnumType.STRING)
    private final String category;

    @Enumerated(EnumType.STRING)
    private final String fragility;

}
