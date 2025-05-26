package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.auth.LoginRequest;
import lv.pakit.dto.response.LoginResponse;
import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthRestController {

    private final IClientRepo clientRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Optional<Client> optionalClient = clientRepo.findByUsername(request.getUsername());

        if (optionalClient.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), optionalClient.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Client client = optionalClient.get();

        LoginResponse response = new LoginResponse("Login successful", client.getUsername());

        return ResponseEntity.ok(response);
    }
}
