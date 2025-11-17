package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.dto.UserDto.UserDto;
import in.lokeshkaushik.to_do_app.dto.UserDto.UserRegistrationDto;
import in.lokeshkaushik.to_do_app.exception.UserAlreadyExistsException;
import in.lokeshkaushik.to_do_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_shouldSaveToDatabase() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "new_user", "newuser@example.com", "password123"
        );

        UserDto saved = userService.registerUser(dto);

        assertNotNull(saved.uuid());
        assertEquals("new_user", saved.username());

        // verify DB persisted it
        assertTrue(userRepository.existsByUsername("new_user"));
    }

    @Test
    void registerUser_shouldFailIfEmailExists() {
        UserRegistrationDto dto1 = new UserRegistrationDto("user1", "same@example.com", "pass");
        UserRegistrationDto dto2 = new UserRegistrationDto("user2", "same@example.com", "pass");

        userService.registerUser(dto1);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(dto2));
    }
}

