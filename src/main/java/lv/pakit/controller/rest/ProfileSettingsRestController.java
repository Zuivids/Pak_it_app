package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.user.PasswordResetRequest;
import lv.pakit.dto.request.user.TotpConfirmRequest;
import lv.pakit.dto.response.user.TotpSetupResponse;
import lv.pakit.service.auth.AuthApiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileSettingsRestController {

    private final AuthApiService authApiService;

    @PostMapping("/2fa/setup")
    public TotpSetupResponse setupTotp() {
        return authApiService.setupTotp();
    }

    @PostMapping("/2fa/confirm")
    public void confirmTotp(@Valid @RequestBody TotpConfirmRequest request) {
        authApiService.confirmTotp(request);
    }

    @PostMapping("/password/reset")
    public void resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        authApiService.resetPassword(request);
    }
}
