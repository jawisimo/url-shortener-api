package com.stackoverblack.urlshortener.util;

import lombok.experimental.UtilityClass;

/**
 * This class provides a set of constant messages used for validation and error handling
 * in the user authentication and URL processing system.
 * These messages are used to give feedback to the user during login, registration,
 * and URL-related operations.
 */
@UtilityClass
public final class MessageProvider {

    // Error and validation messages related to user login and authentication
    public static final String LOGIN_EMPTY_MESSAGE = "Login cannot be empty. ";
    public static final String LOGIN_FORMAT_MESSAGE =
            "Login must be at least 5 and no more than 50 characters " +
                    "and may contain only uppercase and lowercase letters and numbers. ";
    public static final String LOGIN_FORBIDDEN_MESSAGE =
            "It is forbidden to use this login. ";
    public static final String EMAIL_EMPTY_MESSAGE = "Email cannot be empty. ";
    public static final String EMAIL_INCORRECT_MESSAGE =
            "Email is incorrect. ";
    public static final String IDENTIFIER_EMPTY_MESSAGE =
            "Authentication identifier cannot be empty. You must enter your login or email. ";

    // Error messages related to user password validation
    public static final String PASSWORD_EMPTY_MESSAGE = "Password cannot be empty. ";
    public static final String PASSWORD_LIMIT_MESSAGE = "Password size limit exceeded. ";
    public static final String PASSWORD_FORMAT_MESSAGE =
            "Password must be at least 8 characters long, " +
                    "including digits, uppercase, and lowercase letters. ";
    public static final String PASSWORD_WRONG_MESSAGE = "Wrong password. ";

    // General messages related to authentication and authorization
    public static final String UNAUTHORIZED_MESSAGE =
            "User is not authenticated. ";

    // Error messages related to URL validation and processing
    public static final String URL_INCORRECT_MESSAGE = "Url is incorrect. ";
    public static final String URL_NOT_FOUND_MESSAGE = "URL not found. ";
    public static final String TOKEN_INCORRECT_MESSAGE = "JWT token is incorrect. ";
    public static final String URL_EXPIRED_MESSAGE = "URL has expired.";
    public static final String URL_INCORRECT_EXPIRES_AT_MESSAGE =
            "You cannot set the expiration date to a past date. ";

    /**
     * Generates a message indicating that a user with the specified login already exists.
     *
     * @param login the login of the user
     * @return a message indicating the user already exists
     */
    public static String generateUserWithLoginExistsMessage(String login) {
        return "User with login " + login + " already exists. ";
    }

    /**
     * Generates a message indicating that a user with the specified email already exists.
     *
     * @param email the email of the user
     * @return a message indicating the user already exists
     */
    public static String generateUserWithEmailExistsMessage(String email) {
        return "User with email " + email + " already exists. ";
    }

    /**
     * Generates a message indicating that a user with the specified login was not found.
     *
     * @param login the login of the user
     * @return a message indicating the user was not found
     */
    public static String generateUserWithLoginNotFoundMessage(String login) {
        return "User with login " + login + " not found. ";
    }

    /**
     * Generates a message indicating that a user with the specified email was not found.
     *
     * @param email the email of the user
     * @return a message indicating the user was not found
     */
    public static String generateUserWithEmailNotFoundMessage(String email) {
        return "User with email " + email + " not found. ";
    }
}
