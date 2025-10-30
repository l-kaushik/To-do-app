package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.dto.UserDto;
import in.lokeshkaushik.to_do_app.dto.UserLoginDto;
import in.lokeshkaushik.to_do_app.dto.UserRegistrationDto;
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
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto){
        UserDto userDto = userService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    // Get details about user, using UUID
    @GetMapping("/{uuid}")
    public ResponseEntity<UserDto> getUser(@PathVariable @NotNull UUID uuid){
        UserDto userDto = userService.getUser(uuid);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> loginUser(@Valid @RequestBody UserLoginDto loginDto){
        UserDto userDto = userService.loginUser(loginDto);
        return ResponseEntity.ok(userDto);
    }
}
