package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.dto.UserDto;
import in.lokeshkaushik.to_do_app.dto.UserLoginDto;
import in.lokeshkaushik.to_do_app.dto.UserLoginResponseDto;
import in.lokeshkaushik.to_do_app.dto.UserRegistrationDto;
import in.lokeshkaushik.to_do_app.service.JwtService;
import in.lokeshkaushik.to_do_app.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @Test
    void registerUser_ShouldReturnCreatedUser() throws Exception {
        UserDto userDto = new UserDto(UUID.randomUUID(), "testUser", "test@example.com");

        Mockito.when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenReturn(userDto);

        String jsonRequest = """
                {
                    "username": "testUser",
                    "emailId": "test@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.emailId").value("test@example.com"));
    }

    @Test
    void checkUsername_ShouldReturnTrue() throws Exception {
        Mockito.when(userService.isUsernameAvailable("john")).thenReturn(true);

        mockMvc.perform(get("/api/users/check-username")
                        .param("username", "john"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getUser_ShouldReturnUserDetails() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDto userDto = new UserDto(uuid, "john", "john@example.com");

        Mockito.when(userService.getUser(uuid)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.emailId").value("john@example.com"));
    }

    @Test
    void loginUser_ShouldReturnLoginResponse() throws Exception {
        UserLoginResponseDto responseDto = new UserLoginResponseDto( UUID.randomUUID(), "john", "john@example.com", "jwt-token");

        Mockito.when(userService.loginUser(any(UserLoginDto.class)))
                .thenReturn(responseDto);

        String loginJson = """
                {
                    "identifier": "john",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.emailId").value("john@example.com"))
                .andExpect(jsonPath("$.jwtToken").value("jwt-token"));
    }

    @Test
    void checkEmail_ShouldReturnTrue() throws Exception {
        Mockito.when(userService.isEmailAvailable("john@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/users/check-email")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}