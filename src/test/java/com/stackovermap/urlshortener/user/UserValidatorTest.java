package com.stackovermap.urlshortener.user;

import com.stackovermap.urlshortener.error.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stackovermap.urlshortener.util.MessageProvider.*;
import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void init() {
        userValidator = new UserValidator();
    }

    @Test
    void validate_shouldThrowValidationException_withIncorrectLogin() {
        String shortLogin = "11";
        String longLogin = "jK6lM7nO8pQ9rS0tU1vW2xY3zA4B5C6D7E8F9G0H1iJ2K3L4M5N6O7P8Q9R0";
        String failFormatLogin = "?. X/*лсLjdooi24";
        String email = "test@mail.com";
        String password = "Password123";

        ValidationException emptyLoginException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate("", email, password));
        assertEquals(LOGIN_EMPTY_MESSAGE, emptyLoginException.getMessage());

        ValidationException nullLoginException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(null, email, password));
        assertEquals(LOGIN_EMPTY_MESSAGE, nullLoginException.getMessage());

        ValidationException shortLoginException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(shortLogin, email, password));
        assertEquals(LOGIN_FORMAT_MESSAGE, shortLoginException.getMessage());

        ValidationException longLoginException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(longLogin, email, password));
        assertEquals(LOGIN_FORMAT_MESSAGE, longLoginException.getMessage());

        ValidationException failCharFormatLoginException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(failFormatLogin, email, password));
        assertEquals(LOGIN_FORMAT_MESSAGE, failCharFormatLoginException.getMessage());

        ValidationException identifierEmptyException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate("", password));
        assertEquals(IDENTIFIER_EMPTY_MESSAGE, identifierEmptyException.getMessage());
    }

    @Test
    public void validate_shouldThrowValidationException_withIncorrectEmail() {
        String login = "login123";
        String failFormatLEmail = "testm.com";
        String password = "Password123";

        ValidationException emptyEmailException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, "", password));
        assertEquals(EMAIL_EMPTY_MESSAGE, emptyEmailException.getMessage());

        ValidationException nullEmailException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, null, password));
        assertEquals(EMAIL_EMPTY_MESSAGE, nullEmailException.getMessage());

        ValidationException failFormatLEmailException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, failFormatLEmail, password));
        assertEquals(EMAIL_INCORRECT_MESSAGE, failFormatLEmailException.getMessage());
    }

    @Test
    void validate_shouldThrowValidationException_withIncorrectPassword() {
        String login = "login123";
        String email = "test@mail.com";
        String shortPassword = "Pass";
        String longPassword = "A1bC2dE3fG4hI5jK6lM7nO8pQ9rS0tU1vW2xY3zA4B5C6D7E8F9G0H1iJ2K3L4M5N6O7P8Q9R0" +
                "A1bC2dE3fG4hI5jK6lM7nO8pQ9rS0tU1vW2xY3zA4B5C6D7E8F9G0H1iJ2K3L4M5N6O7P8Q9R0";
        String failFormatPassword = "judigkugj";

        ValidationException emptyPasswordException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, email, ""));
        assertEquals(PASSWORD_EMPTY_MESSAGE, emptyPasswordException.getMessage());

        ValidationException nullPasswordException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, email, null));
        assertEquals(PASSWORD_EMPTY_MESSAGE, nullPasswordException.getMessage());

        ValidationException shortPasswordException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, email, shortPassword));
        assertEquals(PASSWORD_FORMAT_MESSAGE, shortPasswordException.getMessage());

        ValidationException longPasswordException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, email, longPassword));
        assertEquals(PASSWORD_LIMIT_MESSAGE, longPasswordException.getMessage());

        ValidationException failFormatPasswordException = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(login, email, failFormatPassword));
        assertEquals(PASSWORD_FORMAT_MESSAGE, failFormatPasswordException.getMessage());
    }

    @Test
    void validate_shouldNotThrowException_whenValidInput() {
        String login = "goit18";
        String email = "test@mail.com";
        String password = "Password123";
        assertDoesNotThrow(() -> userValidator.validate(login, email, password));
    }
}
