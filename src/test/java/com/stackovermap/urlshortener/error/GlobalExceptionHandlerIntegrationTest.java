package com.stackovermap.urlshortener.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackovermap.urlshortener.security.Role;
import com.stackovermap.urlshortener.security.dto.AuthUserRequest;
import com.stackovermap.urlshortener.security.dto.RefreshTokenRequest;
import com.stackovermap.urlshortener.user.User;
import com.stackovermap.urlshortener.user.UserRepository;
import com.stackovermap.urlshortener.user.dto.RegisterUserRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.stackovermap.urlshortener.util.MessageProvider.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static final String USER_REGISTER_PATH = "/api/v1/auth/register";
    private static final String USER_AUTH_PATH = "/api/v1/auth/login";
    private static final String REFRESH_TOKEN_PATH = "/api/v1/auth/refresh";
    private static final String URL_PATH = "/api/v1/url";
    private String login;
    private String incorrectLogin;
    private String email;
    private String password;
    private String wrongPassword;
    private String shortUrlCode;
    private String notFoundLogin;

    @BeforeAll
    void setUp() {
        login = "testUser";
        incorrectLogin = "test";
        email = "test@email.com";
        password = "passWord123";
        wrongPassword = "wrongPassword123";
        shortUrlCode = "J5Ldtoj";
        notFoundLogin = "testUser2";
        User user = User.builder()
                .login(login)
                .email(email)
                .password(password)
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
    }

    @AfterAll
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void handleUserValidateException_shouldReturnBadRequestAndErrorBody() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest(incorrectLogin, email, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(LOGIN_FORMAT_MESSAGE));
    }

    @Test
    void handleUserExistsException_shouldReturnConflictAndErrorBody() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest(login, email, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString(generateUserWithLoginExistsMessage(login))));

    }

    @Test
    void handleBadCredentialsException_shouldReturnUnauthorizedAndErrorBody() throws Exception {
        AuthUserRequest request = new AuthUserRequest(login, wrongPassword);
        mockMvc.perform(post(USER_AUTH_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(PASSWORD_WRONG_MESSAGE));
    }

    @Test
    void handleUnauthorizedException_shouldReturnUnauthorizedAndErrorBody() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("");
        mockMvc.perform(post(REFRESH_TOKEN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(TOKEN_INCORRECT_MESSAGE));
    }

    @Test
    void handleUsernameNotFoundException_shouldReturnUnauthorizedAndErrorBody() throws Exception {
        AuthUserRequest request = new AuthUserRequest(notFoundLogin, password);
        mockMvc.perform(post(USER_AUTH_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(generateUserWithLoginNotFoundMessage(notFoundLogin)));
    }

    @Test
    void handleResourceNotFoundException_shouldReturnNotFoundAndErrorBody() throws Exception {
        mockMvc.perform(post(URL_PATH + "/" + shortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(URL_NOT_FOUND_MESSAGE));
    }
}
