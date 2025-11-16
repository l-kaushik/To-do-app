package in.lokeshkaushik.to_do_app.service;

import com.jayway.jsonpath.JsonPath;
import in.lokeshkaushik.to_do_app.dto.*;
import in.lokeshkaushik.to_do_app.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class WorkspaceServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;
    private final Random random = new Random();

    @BeforeEach
    void mockUser() throws Exception {
        String username = "user_" + random.nextInt(10, 9000);
        String emailId = username + "@example.com";
        String password = username + "_password1234";

        // register user
        UserRegistrationDto dto = new UserRegistrationDto(username, emailId, password);
        UserDto saved = userService.registerUser(dto);
        assertNotNull(saved.uuid());

        // login user
        UserLoginDto loginDto = new UserLoginDto(username, password);
        UserLoginResponseDto loginResponseDto = userService.loginUser(loginDto);

        // get token
        jwtToken = loginResponseDto.jwtToken();
        assertNotNull(jwtToken);
    }

    @Test
    void getWorkspaceIds_shouldTrueForEmptyList() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/workspaces")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();

        // expecting empty list
        List<?> ids = JsonPath.read(json, "$.ids");
        assertTrue(ids.isEmpty());
    }

    @Test
    void getWorkspaceIds_shouldReturnBadRequest() throws Exception {
        String workspaceId = "1";
        MvcResult result = mockMvc.perform(get("/api/workspaces/" + workspaceId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void getWorkspaceIds_ShouldReturnNotFound() throws Exception {
        String workspaceId = "35d81cb5-e9b5-4127-925d-f7dc3d012286";
        MvcResult result = mockMvc.perform(get("/api/workspaces/" + workspaceId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
