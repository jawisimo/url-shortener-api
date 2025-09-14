package com.jawisimo.urlshortener.security;

import com.jawisimo.urlshortener.doc.annotation.security.AuthenticateUserOpenApi;
import com.jawisimo.urlshortener.doc.annotation.security.AuthenticateUserRequestBodyOpenApi;
import com.jawisimo.urlshortener.doc.annotation.security.RefreshUserTokenOpenApi;
import com.jawisimo.urlshortener.doc.annotation.security.RefreshUserTokenRequestBodyOpenApi;
import com.jawisimo.urlshortener.security.dto.AuthUserRequest;
import com.jawisimo.urlshortener.security.dto.AuthUserResponse;
import com.jawisimo.urlshortener.security.dto.RefreshTokenRequest;
import com.jawisimo.urlshortener.security.dto.RefreshTokenResponse;
import com.jawisimo.urlshortener.security.service.JwtAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @AuthenticateUserOpenApi
    @PostMapping("/login")
    public ResponseEntity<AuthUserResponse> authenticateUser(
            @AuthenticateUserRequestBodyOpenApi
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
    @RefreshUserTokenOpenApi
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshUserToken(
            @RefreshUserTokenRequestBodyOpenApi
            @RequestBody
            RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
