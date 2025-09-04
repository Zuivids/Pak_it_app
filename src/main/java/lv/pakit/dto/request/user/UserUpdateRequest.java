package lv.pakit.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 3, max = 100, message = "First name must be between 3 and 100 characters")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ž\\s'-]+$",
            message = "First name must contain only letters, spaces, hyphens, or apostrophes"
    )
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, max = 100, message = "First name must be between 3 and 100 characters")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ž\\s'-]+$",
            message = "Last name must contain only letters, spaces, hyphens, or apostrophes"
    )
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid format")
    private String email;

}