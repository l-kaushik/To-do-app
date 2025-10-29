package in.lokeshkaushik.to_do_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// DTO for getting user data for registration
public record UserRegistrationDto(
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 13, message = "Username must be between 3 and 13 characters.")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores.")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String emailId,

        @NotBlank(message = "Password cannot be empty.")
        @Size(min = 8, max = 128, message = "Password too short or too long.")
        String password) {
}
