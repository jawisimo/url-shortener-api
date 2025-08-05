package com.stackoverblack.urlshortener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackoverblack.urlshortener.security.Role;
import com.stackoverblack.urlshortener.user.dto.RegisterUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.stackoverblack.urlshortener.util.MessageProvider.generateUserWithEmailExistsMessage;
import static com.stackoverblack.urlshortener.util.MessageProvider.generateUserWithLoginExistsMessage;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static final String USER_REGISTER_PATH = "/api/v1/auth/register";
    private String login;
    private String email;
    private String password;
    private String incorrectLogin;
    private String incorrectEmail;
    private User user;

    @BeforeEach
    void setUp() {
        login = "testUser";
        email = "test@email.com";
        password = "passWord123";
        incorrectLogin = "not";
        incorrectEmail = "not@";
        user = User.builder()
                .login(login)
                .email(email)
                .password("passWord123")
                .role(Role.ROLE_USER)
                .build();
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_shouldReturnCreatedAndResponseBody_withSuccessRequest() throws Exception {
        RegisterUserRequest requestSuccess = new RegisterUserRequest(login, email, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSuccess)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value(login))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void registerUser_shouldReturnConflictAndResponseBody_whenLoginAlreadyExists() throws Exception {
        userRepository.save(user);
        RegisterUserRequest requestExistsLogin = new RegisterUserRequest(login, email + "a", password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestExistsLogin)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(generateUserWithLoginExistsMessage(login)));
    }

    @Test
    void registerUser_shouldReturnConflictAndResponseBody_whenEmailAlreadyExists() throws Exception {
        userRepository.save(user);
        RegisterUserRequest requestExistsEmail = new RegisterUserRequest(login + "a", email, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestExistsEmail)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(generateUserWithEmailExistsMessage(email)));
    }

    @Test
    void registerUser_shouldReturnBadRequestAndResponseBody_whenLoginIsIncorrect() throws Exception {
        RegisterUserRequest requestBadLogin = new RegisterUserRequest(incorrectLogin, email, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBadLogin)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_shouldReturnBadRequestAndResponseBody_whenEmailIsIncorrect() throws Exception {
        RegisterUserRequest requestBadEmail = new RegisterUserRequest(login, incorrectEmail, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBadEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_shouldReturnConflictAndResponseBody_whenLoginAndEmailAlreadyExists() throws Exception {
        userRepository.save(user);
        RegisterUserRequest request = new RegisterUserRequest(login, email, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(
                        generateUserWithLoginExistsMessage(login) +
                                generateUserWithEmailExistsMessage(email)));
    }
}
