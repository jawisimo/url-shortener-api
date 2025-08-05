package com.stackovermap.urlshortener.user;

import com.stackovermap.urlshortener.error.exception.UserExistsException;
import com.stackovermap.urlshortener.error.exception.ValidationException;
import com.stackovermap.urlshortener.security.Role;
import com.stackovermap.urlshortener.user.dto.RegisterUserRequest;
import com.stackovermap.urlshortener.user.dto.RegisterUserResponse;
import com.stackovermap.urlshortener.util.MessageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private static User user;
    private static RegisterUserRequest request;

    @BeforeEach
    void init() {
        String login = "login123";
        String password = "passWord123";
        String hashedPassword = "hashPassword123";
        String email = "test@email.com";
        request = new RegisterUserRequest(login, email, password);
        user = User.builder()
                .id(1L)
                .login(login)
                .email(email)
                .password(hashedPassword)
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void register_shouldThrowUserExistsException_whenUserAlreadyExists() {
        when(userRepository.findUserByLoginOrEmail(request.getLogin(), request.getEmail())).thenReturn(Optional.of(user));
        UserExistsException userExistsException = assertThrows(
                UserExistsException.class,
                () -> userService.register(request));
        assertEquals(MessageProvider.generateUserWithLoginExistsMessage(request.getLogin())
                + MessageProvider.generateUserWithEmailExistsMessage(request.getEmail()), userExistsException.getMessage());
    }

    @Test
    void register_shouldThrowValidationException_whenRequestIsNotValid() {
        doThrow(new ValidationException("Request is not valid"))
                .when(userValidator).validate(request.getLogin(), request.getEmail(), request.getPassword());
        ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userService.register(request));
        assertEquals("Request is not valid", validationException.getMessage());
    }

    @Test
    void register_shouldReturnSuccessResponse_whenRequestIsValid() {
        when(userRepository.findUserByLoginOrEmail(request.getLogin(), request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn(user.getPassword());
        RegisterUserResponse response = RegisterUserResponse.createSuccessResponse(request.getLogin(), request.getEmail());
        assertEquals(response, userService.register(request));
    }
}
