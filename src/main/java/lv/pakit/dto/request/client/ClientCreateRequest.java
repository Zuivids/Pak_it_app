package lv.pakit.dto.request.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid format")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "^\\+?\\d{8,20}$",
            message = "Phone number must be valid and contain only digits (optionally starting with +)"
    )
    private String phoneNumber;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ž\\s'-]+$",
            message = "Full name must contain only letters, spaces, hyphens, or apostrophes"
    )
    private String fullName;
}