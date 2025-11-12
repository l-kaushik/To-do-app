package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.model.User;
import in.lokeshkaushik.to_do_app.repository.UserRepository;
import in.lokeshkaushik.to_do_app.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // loads full application
@AutoConfigureMockMvc // sets up MockMvc with all filters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService; // real service bean

//    private String jwtToken;

    @Autowired
    private UserRepository userRepository;

//    @BeforeEach
//    void setUp() {
//        // 👇 generate a real valid token using your JWT service
//        jwtToken = jwtService.generateToken("john");
//    }

    @Test
    void getUser_ShouldReturnUserDetails_WhenValidTokenProvided() throws Exception {
        // initializing data
        User user = User.builder()
                .username("john")
                .emailId("john@example.com")
                .password("strong_password")
                .build();

        UUID uuid = user.getUuid();
        String jwtToken = jwtService.generateToken(uuid.toString());
        userRepository.save(user);

        // performing test
        mockMvc.perform(get("/api/users/{uuid}", uuid)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Optionally check response JSON
        // .andExpect(jsonPath("$.username").value("john"));
    }
}
