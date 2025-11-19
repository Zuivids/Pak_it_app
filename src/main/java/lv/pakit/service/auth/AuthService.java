package lv.pakit.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.model.user.User;
import lv.pakit.model.user.UserRole;
import lv.pakit.security.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static lv.pakit.model.user.UserRole.ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return getUserFromAuthentication(authentication);
    }

    public boolean hasRole(UserRole... roles) {
        final User user = getAuthenticatedUser();

        return Stream.of(roles).anyMatch(role -> role.equals(user.getRole()));
    }

    public boolean isAdmin() {
        final User user = getAuthenticatedUser();

        return ADMIN.equals(user.getRole());
    }

    public Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid username or password");
        }
    }

    public User getUserFromAuthentication(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }
}
