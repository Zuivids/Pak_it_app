package lv.pakit.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class TotpConfirmRequest {

    @NotNull(message = "Missing 2FA code")
    private final String totp;
}
