package lv.pakit.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.request.auth.LoginRequest;
import lv.pakit.dto.request.user.PasswordResetRequest;
import lv.pakit.dto.request.user.TotpConfirmRequest;
import lv.pakit.dto.response.LoginResponse;
import lv.pakit.dto.response.user.TotpSetupResponse;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.exception.http.FieldErrorException;
import lv.pakit.exception.http.InternalErrorException;
import lv.pakit.model.user.User;
import lv.pakit.repo.IUserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthApiService {

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final TotpService totpService;
    private final IUserRepo userRepo;

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        final Authentication authentication = authService.authenticate(request.getUsername(), request.getPassword());

        validateAuthentication(request, authentication);
        handleAuthenticationComplete(authentication, httpRequest);

        return LoginResponse.builder()
                .username(request.getUsername())
                .build();
    }

    public TotpSetupResponse setupTotp() {
        final User user = authService.getAuthenticatedUser();
        final String totpSecret = totpService.generateSecret();

        user.setTotpSecret(totpSecret);
        user.setTotpConfirmed(false);
        userRepo.save(user);

        final String qrCode = totpService.generateQrCode(totpSecret, user.getEmail());

        return TotpSetupResponse.builder()
                .qrCode(qrCode)
                .build();
    }

    public void confirmTotp(TotpConfirmRequest request) {
        final User user = authService.getAuthenticatedUser();

        if (user.getTotpSecret() == null) {
            throw new InternalErrorException("Totp secret not set up for user");
        }
        if (!totpService.codeIsValid(user.getTotpSecret(), request.getTotp())) {
            throw new BadRequestException("Invalid 2FA code");
        }

        user.setTotpConfirmed(true);
        userRepo.save(user);
    }

    public void resetPassword(PasswordResetRequest request) {
        final User user = authService.getAuthenticatedUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new FieldErrorException("oldPassword", "Invalid password");
        }
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new FieldErrorException("newPasswordConfirm", "New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }

    private void validateAuthentication(LoginRequest request, Authentication authentication) {
        final User user = authService.getUserFromAuthentication(authentication);
        if (user.isTotp()) {
            validateTotp(request, user);
        }
    }

    private void handleAuthenticationComplete(Authentication authentication, HttpServletRequest httpRequest) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpRequest.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    private void validateTotp(LoginRequest request, User user) {
        if (request.getTotp() == null) {
            throw new FieldErrorException("totp", "Missing 2FA code");
        }
        if (!totpService.codeIsValid(user.getTotpSecret(), request.getTotp())) {
            throw new FieldErrorException("totp", "Invalid 2FA code");
        }
    }
}
