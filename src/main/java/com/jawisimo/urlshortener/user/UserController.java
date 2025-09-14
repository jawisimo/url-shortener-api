package com.jawisimo.urlshortener.user;

import com.jawisimo.urlshortener.doc.annotation.user.RegisterUserOpenApi;
import com.jawisimo.urlshortener.doc.annotation.user.RegisterUserRequestBodyOpenApi;
import com.jawisimo.urlshortener.user.dto.RegisterUserRequest;
import com.jawisimo.urlshortener.user.dto.RegisterUserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for user authentication and registration endpoints.
 * <p>
 * This class handles the HTTP requests related to user authentication, including registering new users.
 * It also handles validation of user input and provides relevant responses, including success and error messages.
 * </p>
 */
@Tag(name = "1. Authentication", description = "Endpoints for user authentication and registration")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Registers a new user with provided credentials.
     * <p>
     * This endpoint registers a new user by accepting a {@link RegisterUserRequest} in the request body,
     * and returns a {@link RegisterUserResponse} with the user's registration details if successful.
     * </p>
     *
     * @param request the user's registration request containing necessary details (login, email, password)
     * @return a {@link ResponseEntity} containing the registration response with status 201 (CREATED)
     */
    @RegisterUserOpenApi
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(
            @RegisterUserRequestBodyOpenApi
            @RequestBody RegisterUserRequest request) {
        RegisterUserResponse response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
