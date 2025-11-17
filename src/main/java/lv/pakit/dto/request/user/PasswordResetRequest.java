package lv.pakit.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class PasswordResetRequest {

    @NotBlank(message = "Missing old password")
    private final String oldPassword;
    @NotBlank(message = "Missing new password")
    private final String newPassword;
    @NotBlank(message = "Missing new password confirmation")
    private final String newPasswordConfirm;
}
