package odimash.openforum.security;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import odimash.openforum.domain.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Autowired
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .flatMap(role -> role.getRights().stream())
                .map(RightGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    // Остальные методы UserDetails (isAccountNonExpired, isAccountNonLocked, etc.)

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return true; // Или можешь использовать логику проверки активности пользователя
    }
}
