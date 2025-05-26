package lv.pakit.security;

import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IClientRepo clientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found: " + username));
        return new CustomUserDetails(client);
    }
}
