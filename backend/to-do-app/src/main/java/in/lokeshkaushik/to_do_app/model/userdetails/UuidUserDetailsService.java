package in.lokeshkaushik.to_do_app.model.userdetails;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UuidUserDetailsService extends UserDetailsService {
    UuidUserDetails loadUserByIdentifier(String identifier);
}
