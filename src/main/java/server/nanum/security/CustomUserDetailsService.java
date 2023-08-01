package server.nanum.security;

import jakarta.persistence.EntityGraph;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import server.nanum.domain.User;
import server.nanum.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // UserWrapper 객체를 만들어 반환
        return new UserWrapper(user);
    }
}


