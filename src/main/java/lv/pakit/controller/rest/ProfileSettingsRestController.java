package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.user.TotpConfirmRequest;
import lv.pakit.dto.response.user.TotpSetupResponse;
import lv.pakit.model.User;
import lv.pakit.service.AuthService;
import lv.pakit.service.TotpService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile/settings/")
public class ProfileSettingsRestController {

    private final AuthService authService;
    private final TotpService totpService;

    @PostMapping("/2fa/setup")
    public TotpSetupResponse setupTotp() {
        final User user = authService.getAuthenticatedUser();

        return totpService.setupTotp(user.getUserId());
    }

    @PostMapping("/2fa/confirm")
    public void confirmTotp(@Valid @RequestBody TotpConfirmRequest request) {
        final User user = authService.getAuthenticatedUser();
        totpService.confirmTotp(user.getUserId(), request);
    }
}
