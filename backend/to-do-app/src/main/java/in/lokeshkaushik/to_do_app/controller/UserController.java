package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.dto.UserDto;
import in.lokeshkaushik.to_do_app.dto.UserRegistrationDto;
import in.lokeshkaushik.to_do_app.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto){
        try{
            UserDto userDto = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
