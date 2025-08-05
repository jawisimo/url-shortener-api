package com.ikg100.urlshortenerapi.user;

import com.ikg100.urlshortenerapi.error.exception.UserExistsException;
import com.ikg100.urlshortenerapi.security.Role;
import com.ikg100.urlshortenerapi.user.dto.RegisterUserRequest;
import com.ikg100.urlshortenerapi.user.dto.RegisterUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ikg100.urlshortenerapi.util.MessageProvider.generateUserWithEmailExistsMessage;
import static com.ikg100.urlshortenerapi.util.MessageProvider.generateUserWithLoginExistsMessage;

/**
 * Service responsible for user registration, including validation and persistence.
 * <p>
 * This class handles the user registration logic, including validation of the provided input,
 * checking for existing users, and saving the new user into the database.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with the provided registration details.
     * <p>
     * The method validates the user's input, checks if a user with the same login or email already exists,
     * and if not, it saves the new user in the database.
     * </p>
     *
     * @param request the registration request containing user details such as login, email, and password
     * @return a response containing the newly registered user's login and email
     * @throws UserExistsException if a user with the same login or email already exists
     */
    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        String login = request.getLogin();
        String email = request.getEmail();
        String password = request.getPassword();

        // Validate input data
        userValidator.validate(login, email, password);

        // Check if the user already exists
        Optional<User> userOptional = userRepository.findUserByLoginOrEmail(login, email);
        userOptional.ifPresent(user -> checkUserExists(user, login, email));

        // Encrypt password
        String hashedPassword = passwordEncoder.encode(password);

        // Save the new user to the database
        userRepository.save(User.builder()
                .login(login)
                .email(email)
                .password(hashedPassword)
                .role(Role.ROLE_USER)  // Default role is ROLE_USER
                .build());

        // Return a successful registration response
        return RegisterUserResponse.createSuccessResponse(login, email);
    }

    /**
     * Checks if a user already exists based on the provided login and email.
     * If a user with the same login or email exists, it throws a {@link UserExistsException}.
     *
     * @param user the user to check for existence
     * @param login the user's login
     * @param email the user's email
     */
    private void checkUserExists(User user, String login, String email) {
        StringBuilder errorMessage = new StringBuilder();

        // Check if the login already exists
        if (user.getLogin().equals(login)) {
            errorMessage.append(generateUserWithLoginExistsMessage(login));
        }

        // Check if the email already exists
        if (user.getEmail().equals(email)) {
            errorMessage.append(generateUserWithEmailExistsMessage(email));
        }

        // If any error messages were added, throw an exception with the appropriate message
        if (!errorMessage.isEmpty()) {
            throw new UserExistsException(errorMessage.toString());
        }
    }
}
