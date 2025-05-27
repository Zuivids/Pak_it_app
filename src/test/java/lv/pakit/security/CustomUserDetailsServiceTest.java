package lv.pakit.security;

import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService service;
    @MockitoBean
    private IClientRepo clientRepo;

    @Test
    void shouldLoadUserByUsername() {
        Client client = new Client();
        client.setUsername("Valids");
        client.setPassword("hashed-password");

        when(clientRepo.findByUsername("Valids")).thenReturn(Optional.of(client));

        var userDetails = service.loadUserByUsername("Valids");

        assertEquals("Valids", userDetails.getUsername());
        assertEquals("hashed-password", userDetails.getPassword());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(clientRepo.findByUsername("Testeris")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("Testeris");
        });
    }
}
