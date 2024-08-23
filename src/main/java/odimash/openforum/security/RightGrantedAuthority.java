package odimash.openforum.security;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import odimash.openforum.domain.entity.Rights;

@AllArgsConstructor
public class RightGrantedAuthority implements GrantedAuthority {

    private final Rights right;

    @Override
    public String getAuthority() {
        return right.name();
    }
}
