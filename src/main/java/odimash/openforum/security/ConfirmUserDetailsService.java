package odimash.openforum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import odimash.openforum.domain.repository.UserRepository;

public class ConfirmUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(ConfirmUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException("User \'" + username + "\" not found"));

    }

}
