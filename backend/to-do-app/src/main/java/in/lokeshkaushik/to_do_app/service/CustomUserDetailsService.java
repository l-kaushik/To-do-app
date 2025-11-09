package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.exception.InvalidCredentialsException;
import in.lokeshkaushik.to_do_app.model.User;
import in.lokeshkaushik.to_do_app.model.UserPrincipal;
import in.lokeshkaushik.to_do_app.model.userdetails.UuidUserDetails;
import in.lokeshkaushik.to_do_app.model.userdetails.UuidUserDetailsService;
import in.lokeshkaushik.to_do_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UuidUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UuidUserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = null;

        if(identifier.contains("@")){
            user = userRepository.findByEmailId(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid email"));
        }
        else if(identifier.contains("-")){
            user = userRepository.findByUuid(UUID.fromString(identifier))
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid UUID"));
        }
        else{
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));
        }
        return new UserPrincipal(user);
    }

    @Override
    public UuidUserDetails loadUserByIdentifier(String identifier) {
        return loadUserByUsername(identifier);
    }
}
