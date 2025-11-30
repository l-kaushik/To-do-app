package in.lokeshkaushik.to_do_app.model;

import in.lokeshkaushik.to_do_app.model.userdetails.UuidUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(User user) implements UuidUserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getUuid() {
        return user.getUuid().toString();
    }
}
