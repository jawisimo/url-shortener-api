package com.stackovermap.urlshortener.user;

import com.stackovermap.urlshortener.error.exception.ValidationException;
import com.stackovermap.urlshortener.user.dto.RegisterUserRequest;
import com.stackovermap.urlshortener.user.dto.RegisterUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private static String login;
    private static String email;
    private static String password;

    @BeforeEach
    void init() {
        login = "login123";
        email = "test@email.com";
        password = "passWord123";
    }

    @Test
    void registerUser_shouldReturnCreated_whenSuccessRequest() {
        RegisterUserRequest request = new RegisterUserRequest(login, password, email);
        RegisterUserResponse expectedResponse = new RegisterUserResponse(login, email);

        when(userService.register(request)).thenReturn(expectedResponse);

        ResponseEntity<RegisterUserResponse> actualResponse = userController.registerUser(request);

        assertEquals(201, actualResponse.getStatusCode().value());
        assertNotNull(actualResponse.getBody());
        assertEquals(expectedResponse.getLogin(), actualResponse.getBody().getLogin());
        assertEquals(expectedResponse.getEmail(), actualResponse.getBody().getEmail());
        verify(userService, times(1)).register(request);
    }

    @Test
    void registerUser_shouldThrowException_whenInvalidRequest() {
        RegisterUserRequest request = new RegisterUserRequest(login, password, email);
        when(userService.register(request)).thenThrow(
                new ValidationException("Login is incorrect"));
        assertThrows(ValidationException.class, () -> userController.registerUser(request));
        verify(userService, times(1)).register(request);
    }
}
