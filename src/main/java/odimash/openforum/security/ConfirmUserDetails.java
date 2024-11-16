package odimash.openforum.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import odimash.openforum.domain.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfirmUserDetails implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmUserDetails.class);

    private User user;

    public ConfirmUserDetails(User user) {
        this.user = user;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getRights().stream())
                .map(right -> new SimpleGrantedAuthority(right.name()))
                .collect(Collectors.toList());

        authorities.forEach(auth -> logger.info("Granted Authority: {}", user.getRoles()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

}
