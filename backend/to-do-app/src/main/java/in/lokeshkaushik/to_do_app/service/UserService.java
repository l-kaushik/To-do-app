package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.dto.UserDto;
import in.lokeshkaushik.to_do_app.dto.UserRegistrationDto;
import in.lokeshkaushik.to_do_app.model.User;
import in.lokeshkaushik.to_do_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto registerUser(UserRegistrationDto registrationDto){
        if(registrationDto.username().isEmpty() ||
        registrationDto.emailId().isEmpty() ||
        registrationDto.password().isEmpty()) {
            throw new IllegalArgumentException("Username, email, and password cannot be empty.");
        }

        if(userRepository.existsByEmailId(registrationDto.emailId())){
            throw new IllegalArgumentException("Email already registered");
        }

        if(userRepository.existsByUsername(registrationDto.username())){
            throw new IllegalArgumentException("Username already registered");
        }

        User user = User.builder()
                .username(registrationDto.username())
                .emailId(registrationDto.emailId())
                .password(registrationDto.password())
                .build();

        User saved = userRepository.save(user);

        if(saved.getId() == null){
            throw new RuntimeException("Failed to save user");
        }

        return new UserDto(saved.getUuid(), saved.getUsername(), saved.getEmailId());
    }
}
