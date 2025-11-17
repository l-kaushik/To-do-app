package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.config.SecurityConfig;
import in.lokeshkaushik.to_do_app.dto.UserDto.*;
import in.lokeshkaushik.to_do_app.exception.InvalidCredentialsException;
import in.lokeshkaushik.to_do_app.exception.NoChangesDetectedException;
import in.lokeshkaushik.to_do_app.exception.UserAlreadyExistsException;
import in.lokeshkaushik.to_do_app.exception.UserNotFoundException;
import in.lokeshkaushik.to_do_app.model.User;
import in.lokeshkaushik.to_do_app.model.UserPrincipal;
import in.lokeshkaushik.to_do_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

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

    @Transactional
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

        return userToUserDto(saved);
    }

    public UserDto getUser(UUID uuid){
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));
        return userToUserDto(user);
    }

    public UserLoginResponseDto loginUser(@Valid UserLoginDto loginDto){
        try{
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.identifier(), loginDto.password()));
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.user();
            String jwtToken = jwtService.generateToken(user.getUuid().toString());

            return new UserLoginResponseDto(user.getUuid(), user.getUsername(), user.getEmailId(), jwtToken);
        }
        catch (BadCredentialsException | InternalAuthenticationServiceException e){
            String message = Objects.equals(e.getMessage(), "Bad credentials") ? "Invalid password" : e.getMessage();
            throw new InvalidCredentialsException(message + ", please re-verify entered information");
        }
    }

    @Transactional
    public UserDto updateUser(@Valid UserUpdateDto updateDto) {
        var username = updateDto.username();
        var emailId = updateDto.emailId();
        var password = updateDto.password();

        if(isAllBlank(username, emailId, password)){
            throw new NoChangesDetectedException("No data found for update.");
        }

        User existing = getCurrentAuthenticatedUser();
        boolean changed = false;

        changed |= updateFieldIfChanged(username, existing.getUsername(), newUsername -> {
            if(userRepository.existsByUsername(newUsername)){
                throw new UserAlreadyExistsException("Username already exists");
            }
            else{
                existing.setUsername(updateDto.username());
            }
        });

        changed |= updateFieldIfChanged(emailId, existing.getEmailId(), newEmailId -> {
            if(userRepository.existsByEmailId(newEmailId)){
                throw new UserAlreadyExistsException("EmailId already exists");
            }
            else{
                existing.setEmailId(updateDto.emailId());
            }
        });

        if(isNotBlank(password)){
            existing.setPassword(securityConfig.passwordEncoder().encode(updateDto.password()));
        }

        if(!changed){
            throw new NoChangesDetectedException("No changes detected for update.");
        }

        User savedUser = userRepository.save(existing);
        return userToUserDto(savedUser);
    }

    public User getCurrentAuthenticatedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserPrincipal) principal).user().getUsername();
        } else {
            // fall back if instanceof failed
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with provided information"));
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmailId(email);
    }

    private boolean isNotBlank(String s){
        return s != null && !s.trim().isEmpty();
    }

    private boolean isAllBlank(String... values){
        return Arrays.stream(values).noneMatch(this::isNotBlank);
    }

    // provided by ChatGPT ofc
    // I did implement but a lot of if-else so
    // basically update if value is new and changed
    // when criteria matches it run the lines from lambda defined when calling this method.
    private boolean updateFieldIfChanged(String newValue, String currentValue, Consumer<String> updater){
        if(isNotBlank(newValue) && !newValue.equals(currentValue)){
            updater.accept(newValue);
            return true;
        }
        return false;
    }

    private UserDto userToUserDto(User user){
       return new UserDto(user.getUuid(), user.getUsername(), user.getEmailId());
    }
}
