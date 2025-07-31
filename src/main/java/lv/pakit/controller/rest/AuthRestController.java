package lv.pakit.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.auth.LoginRequest;
import lv.pakit.dto.response.LoginResponse;
import lv.pakit.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Authentication rawAuth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) rawAuth.getPrincipal();

            String role = userDetails.getUser().getRole();
            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(role)
            );

            Authentication fullAuth = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), authorities
            );

            SecurityContextHolder.getContext().setAuthentication(fullAuth);
            httpRequest.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return ResponseEntity.ok(new LoginResponse("Pieslēgšanās veiksmīga!", userDetails.getUsername(), role));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Nepareizs lietotājvārds vai parole.", null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        request.getSession(false).invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

}
