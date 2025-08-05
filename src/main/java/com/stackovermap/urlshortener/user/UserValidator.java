package com.stackovermap.urlshortener.user;

import com.stackovermap.urlshortener.error.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.stackovermap.urlshortener.security.AuthIdentifierType.detectType;
import static com.stackovermap.urlshortener.util.MessageProvider.*;

/**
 * A validator class for validating user registration and authentication inputs.
 * This class validates the login, email, and password according to predefined rules.
 */
@Component
public class UserValidator {
    private static final int MIN_LOGIN_LENGTH = 5;
    private static final int MAX_LOGIN_LENGTH = 50;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 100;

    /**
     * Validates the provided login, email, and password during user registration.
     * Throws a ValidationException if any of the input fields are invalid.
     *
     * @param login    the user's login
     * @param email    the user's email
     * @param password the user's password
     * @throws ValidationException if any validation fails
     */
    public void validate(String login, String email, String password) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        errors.append(validateLogin(login));
        errors.append(validateEmail(email));
        errors.append(validatePassword(password));

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }

    /**
     * Validates the identifier (either login or email) and password during user authentication.
     * Throws a ValidationException if any of the input fields are invalid.
     *
     * @param identifier the identifier (either login or email)
     * @param password   the user's password
     * @throws ValidationException if any validation fails
     */
    public void validate(String identifier, String password) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        switch (detectType(identifier)) {
            case LOGIN -> errors.append(validateLogin(identifier));
            case EMAIL -> errors.append(validateEmail(identifier));
            default -> errors.append(IDENTIFIER_EMPTY_MESSAGE);
        }

        errors.append(validatePassword(password));

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }


    /**
     * Validates the login based on length and format constraints.
     * The login must not be empty, cannot be "anonymousUser", and should only contain alphanumeric characters.
     * It must also fall within the specified length range.
     *
     * @param login the user's login
     * @return error message if invalid, otherwise an empty string
     */
    private String validateLogin(String login) {
        StringBuilder errors = new StringBuilder();

        if (Objects.isNull(login) || login.isEmpty()) {
            errors.append(LOGIN_EMPTY_MESSAGE);
            return errors.toString();
        }

        if (login.equalsIgnoreCase("anonymousUser")) {
            errors.append(LOGIN_FORBIDDEN_MESSAGE);
            return errors.toString();
        }

        if (!login.matches("^[a-zA-Z0-9]+$")
                || (login.length() < MIN_LOGIN_LENGTH
                || login.length() > MAX_LOGIN_LENGTH)) {
            errors.append(LOGIN_FORMAT_MESSAGE);
        }

        return errors.toString();
    }

    /**
     * Validates the email format.
     * The email must not be empty and must match the standard email format.
     *
     * @param email the user's email
     * @return error message if invalid, otherwise an empty string
     */
    private String validateEmail(String email) {
        StringBuilder errors = new StringBuilder();

        if (Objects.isNull(email) || email.isEmpty()) {
            errors.append(EMAIL_EMPTY_MESSAGE);
            return errors.toString();
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,}$")) {
            errors.append(EMAIL_INCORRECT_MESSAGE);
        }

        return errors.toString();
    }

    /**
     * Validates the password based on length and format constraints.
     * The password must be between the minimum and maximum lengths and must contain at least one lowercase letter,
     * one uppercase letter, and one digit.
     *
     * @param password the user's password
     * @return error message if invalid, otherwise an empty string
     */
    private String validatePassword(String password) {
        StringBuilder errors = new StringBuilder();

        if (Objects.isNull(password) || password.isEmpty()) {
            errors.append(PASSWORD_EMPTY_MESSAGE);
            return errors.toString();
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            errors.append(PASSWORD_LIMIT_MESSAGE);
            return errors.toString();
        }

        if (password.length() < MIN_PASSWORD_LENGTH
                || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
            errors.append(PASSWORD_FORMAT_MESSAGE);
        }

        return errors.toString();
    }
}
