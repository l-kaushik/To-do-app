package in.lokeshkaushik.to_do_app.model.userdetails;

import in.lokeshkaushik.to_do_app.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UuidUserDetails extends UserDetails {
    String getUuid();
}
