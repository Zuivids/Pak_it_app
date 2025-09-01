package lv.pakit.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.request.auth.LoginRequest;
import lv.pakit.dto.response.LoginResponse;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.exception.http.FieldErrorException;
import lv.pakit.model.User;
import lv.pakit.security.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TotpService totpService;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return getUserFromAuthentication(authentication);
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        final Authentication authentication = authenticate(request.getUsername(), request.getPassword());

        validateAuthentication(request, authentication);
        handleAuthenticationComplete(authentication, httpRequest);

        return LoginResponse.builder()
                .username(request.getUsername())
                .build();
    }

    private Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid username or password");
        }
    }

    private void validateAuthentication(LoginRequest request, Authentication authentication) {
        final User user = getUserFromAuthentication(authentication);
        if (user.isTotp()) {
            validateTotp(request, user);
        }
    }

    private User getUserFromAuthentication(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    private void validateTotp(LoginRequest request, User user) {
        if (request.getTotp() == null) {
            throw new FieldErrorException("totp", "Missing 2FA code");
        }
        if (!totpService.codeIsValid(user.getTotpSecret(), request.getTotp())) {
            throw new FieldErrorException("totp", "Invalid 2FA code");
        }
    }

    private void handleAuthenticationComplete(Authentication authentication, HttpServletRequest httpRequest) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpRequest.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }
}
