package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.dto.UserDto.*;
import in.lokeshkaushik.to_do_app.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register or create user account
    // TODO: Implement captcha, CORS to only allow access from frontend side
    @PostMapping("/")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto){
        UserDto userDto = userService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    // Update user details
    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserUpdateDto updateDto){
        UserDto userDto = userService.updateUser(updateDto);
        return ResponseEntity.ok(userDto);
    }

    // TODO: update to single /check endpoint
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username){
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email){
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(available);
    }

    // Get details about user, using UUID
    @GetMapping("/{uuid}")
    public ResponseEntity<UserDto> getUser(@PathVariable @NotNull UUID uuid){
        UserDto userDto = userService.getUser(uuid);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> loginUser(@Valid @RequestBody UserLoginDto loginDto){
        UserLoginResponseDto userLoginResponseDto = userService.loginUser(loginDto);
        return ResponseEntity.ok(userLoginResponseDto);
    }
}
