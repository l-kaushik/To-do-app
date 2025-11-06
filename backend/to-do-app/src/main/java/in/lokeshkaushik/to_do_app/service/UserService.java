package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.config.SecurityConfig;
import in.lokeshkaushik.to_do_app.dto.*;
import in.lokeshkaushik.to_do_app.exception.InvalidCredentialsException;
import in.lokeshkaushik.to_do_app.exception.UserAlreadyExistsException;
import in.lokeshkaushik.to_do_app.exception.UserNotFoundException;
import in.lokeshkaushik.to_do_app.model.User;
import in.lokeshkaushik.to_do_app.model.UserPrincipal;
import in.lokeshkaushik.to_do_app.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

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
                .password(securityConfig.passwordEncoder().encode(registrationDto.password()))
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

    public UserLoginResponseDto loginUser(@Valid UserLoginDto loginDto){
        try{
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.identifier(), loginDto.password()));
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.getUser();
            String jwtToken = jwtService.generateToken(userPrincipal.getUsername());

            return new UserLoginResponseDto(user.getUuid(), user.getUsername(), user.getEmailId(), jwtToken);
        }
        catch (BadCredentialsException | InternalAuthenticationServiceException e){
            String message = Objects.equals(e.getMessage(), "Bad credentials") ? "Invalid password" : e.getMessage();
            throw new InvalidCredentialsException(message + ", please re-verify entered information");
        }
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
