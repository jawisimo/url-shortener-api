package com.stackoverblack.urlshortener.security.service;

import com.stackoverblack.urlshortener.error.exception.UnauthorizedException;
import com.stackoverblack.urlshortener.security.dto.AuthUserRequest;
import com.stackoverblack.urlshortener.security.dto.AuthUserResponse;
import com.stackoverblack.urlshortener.security.dto.RefreshTokenRequest;
import com.stackoverblack.urlshortener.security.dto.RefreshTokenResponse;
import com.stackoverblack.urlshortener.security.provider.JwtTokenProvider;
import com.stackoverblack.urlshortener.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.stackoverblack.urlshortener.util.MessageProvider.TOKEN_INCORRECT_MESSAGE;

/**
 * Service responsible for handling user authentication using JWT tokens.
 * <p>
 * Provides methods to authenticate a user and to refresh tokens.
 */
@Service
@RequiredArgsConstructor
public class JwtAuthenticationService implements AuthenticationService {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;

    /**
     * Authenticates a user based on login/email and password.
     * <p>
     * If authentication is successful, returns an access token and a refresh token.
     *
     * @param request the authentication request containing identifier and password
     * @return {@link AuthUserResponse} with JWT access and refresh tokens
     */
    @Override
    @Transactional(readOnly = true)
    public AuthUserResponse authenticate(AuthUserRequest request) {
        String identifier = request.getIdentifier();
        String password = request.getPassword();

        // Validate that identifier and password are not blank or invalid
        userValidator.validate(identifier, password);

        // Authenticate using Spring Security
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(identifier, password);
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate tokens
        String authLogin = authentication.getName();
        String accessToken = tokenProvider.generateAccessToken(authLogin);
        String refreshToken = tokenProvider.generateRefreshToken(authLogin);

        return AuthUserResponse.createSuccessResponse(accessToken, refreshToken);
    }

    /**
     * Refreshes the access and refresh tokens based on a valid refresh token.
     *
     * @param request the refresh token request
     * @return {@link RefreshTokenResponse} with new tokens
     * @throws UnauthorizedException if the provided token is invalid or expired
     */
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestToken = request.getRefreshToken();

        // Validate refresh token
        if (tokenProvider.validateToken(requestToken)) {
            String login = tokenProvider.getLoginFromToken(requestToken);

            // Generate new tokens
            String accessToken = tokenProvider.generateAccessToken(login);
            String refreshToken = tokenProvider.generateRefreshToken(login);

            return RefreshTokenResponse.createSuccessResponse(accessToken, refreshToken);
        }

        throw new UnauthorizedException(TOKEN_INCORRECT_MESSAGE);
    }
}
