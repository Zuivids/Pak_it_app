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

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "Username can only contain letters, numbers, dots, dashes, and underscores")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).+$",
            message = "Password must contain upper and lower case letters, a number, and a special character"
    )
    private String password;

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