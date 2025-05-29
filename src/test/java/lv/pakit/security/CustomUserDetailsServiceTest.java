package lv.pakit.security;

import lv.pakit.model.User;
import lv.pakit.repo.IUserRepo;
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
    private IUserRepo userRepo;

    @Test
    void shouldLoadUserByUsername() {
        User user = new User();
        user.setUsername("Valids");
        user.setPassword("hashed-password");

        when(userRepo.findByUsername("Valids")).thenReturn(Optional.of(user));

        var userDetails = service.loadUserByUsername("Valids");

        assertEquals("Valids", userDetails.getUsername());
        assertEquals("hashed-password", userDetails.getPassword());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(userRepo.findByUsername("Testeris")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("Testeris");
        });
    }
}
