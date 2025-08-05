package com.stackovermap.urlshortener.security;

import com.stackovermap.urlshortener.error.ErrorResponse;
import com.stackovermap.urlshortener.security.dto.AuthUserRequest;
import com.stackovermap.urlshortener.security.dto.AuthUserResponse;
import com.stackovermap.urlshortener.security.dto.RefreshTokenRequest;
import com.stackovermap.urlshortener.security.dto.RefreshTokenResponse;
import com.stackovermap.urlshortener.security.service.JwtAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling authentication-related requests.
 * <p>
 * Provides endpoints for user login and token refreshing.
 */
@Tag(name = "1. Authentication", description = "Endpoints for user authentication and registration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final JwtAuthenticationService authService;

    /**
     * Authenticates a user based on provided identifier (login or email) and password.
     * <p>
     * Returns a pair of JWT tokens (access and refresh) if authentication is successful.
     *
     * @param request the authentication request containing identifier and password
     * @return {@link ResponseEntity} with {@link AuthUserResponse} and HTTP 200 status
     */
    @Operation(
            summary = "User log in",
            description = "Authenticates a user by login or email and returns the JWT access token and refresh token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully authenticated",
                    content = @Content(schema = @Schema(implementation = AuthUserResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Input data is not correct (e.g., incorrect login, email or password format)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BadRequestExample",
                                    ref = "#/components/examples/BadRequestExample"))),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authenticated (e.g., wrong password)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "UnauthorizedExample",
                                    ref = "#/components/examples/UnauthorizedExample")))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthUserResponse> authenticateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = AuthUserRequest.class)))
            @RequestBody
            AuthUserRequest request) {
        AuthUserResponse response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Refreshes JWT tokens using the provided refresh token.
     * <p>
     * If the refresh token is valid, returns a new pair of access and refresh tokens.
     *
     * @param request the refresh token request
     * @return {@link ResponseEntity} with {@link RefreshTokenResponse} and HTTP 200 status
     */
    @Operation(
            summary = "User refresh token",
            description = "Refresh user JWT token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "JWT token was refreshed successfully",
                    content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "JWT token is not valid",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "UnauthorizedExample",
                                    ref = "#/components/examples/UnauthorizedExample")))
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshUserToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class)))
            @RequestBody
            RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
