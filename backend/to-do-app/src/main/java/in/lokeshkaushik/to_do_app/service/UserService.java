package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.dto.UserDto;
import in.lokeshkaushik.to_do_app.dto.UserLoginDto;
import in.lokeshkaushik.to_do_app.dto.UserRegistrationDto;
import in.lokeshkaushik.to_do_app.dto.UserUpdateDto;
import in.lokeshkaushik.to_do_app.exception.InvalidCredentialsException;
import in.lokeshkaushik.to_do_app.exception.UserAlreadyExistsException;
import in.lokeshkaushik.to_do_app.exception.UserNotFoundException;
import in.lokeshkaushik.to_do_app.model.User;
import in.lokeshkaushik.to_do_app.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto registerUser(UserRegistrationDto registrationDto){
        if(userRepository.existsByEmailId(registrationDto.emailId())){
            throw new UserAlreadyExistsException("Email already registered");
        }

        if(userRepository.existsByUsername(registrationDto.username())){
            throw new UserAlreadyExistsException("Username already registered");
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

    public UserDto getUser(UUID uuid){
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));
        return new UserDto(user.getUuid(), user.getUsername(), user.getEmailId());
    }

    public UserDto loginUser(@Valid UserLoginDto loginDto){
        String identifier = loginDto.identifier();
        User user = null;

        if(identifier.contains("@")){
            user = userRepository.findByEmailId(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid email"));
        }
        else{
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));
        }

        if(!loginDto.password().matches(user.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }

        return new UserDto(user.getUuid(), user.getUsername(), user.getEmailId());
    }

    public UserDto updateUser(UUID uuid, @Valid UserUpdateDto updateDto) {
        System.out.println("update user service called");
        User existing = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));

        if (!existing.getUsername().equals(updateDto.username())
                && userRepository.existsByUsername(updateDto.username())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if(!existing.getEmailId().equals(updateDto.emailId())
                && userRepository.existsByEmailId(updateDto.emailId())){
            throw new UserAlreadyExistsException("EmailId already exists");
        }

        // update
        existing.setUsername(updateDto.username());
        existing.setEmailId(updateDto.emailId());
        // TODO: Add password encoding
        existing.setPassword(updateDto.password());

        User savedUser = userRepository.save(existing);
        return new UserDto(savedUser.getUuid(), savedUser.getUsername(), savedUser.getEmailId());
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmailId(email);
    }
}
