package com.stackovermap.urlshortener.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackovermap.urlshortener.security.dto.AuthUserRequest;
import com.stackovermap.urlshortener.user.UserRepository;
import com.stackovermap.urlshortener.user.dto.RegisterUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.stackovermap.urlshortener.util.MessageProvider.generateUserWithLoginNotFoundMessage;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static final String USER_REGISTER_PATH = "/api/v1/auth/register";
    private static final String USER_AUTH_PATH = "/api/v1/auth/login";

    private String login;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        login = "testUser";
        email = "test@email.com";
        password = "passWord123";
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void authenticateUser_shouldReturnOk_withSuccessRequest() throws Exception {
        RegisterUserRequest requestRegister = new RegisterUserRequest(login, email, password);
        mockMvc.perform(post(USER_REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestRegister)));
        AuthUserRequest requestAuth = new AuthUserRequest(login, password);
        mockMvc.perform(post(USER_AUTH_PATH)
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestAuth)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUser_shouldReturnUnauthorized_whenUserDoesNotExist() throws Exception {
        AuthUserRequest requestAuth = new AuthUserRequest(login, password);
        mockMvc.perform(post(USER_AUTH_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAuth)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value(generateUserWithLoginNotFoundMessage(login)));
    }
}
