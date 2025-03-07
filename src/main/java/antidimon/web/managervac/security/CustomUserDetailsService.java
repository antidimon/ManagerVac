package antidimon.web.managervac.security;

import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.repositories.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MyUser user = myUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
