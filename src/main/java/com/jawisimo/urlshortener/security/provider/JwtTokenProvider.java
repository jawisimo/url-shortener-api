package com.jawisimo.urlshortener.security.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

/**
 * Provider class for generating, parsing and validating JWT access and refresh tokens.
 * Uses HS256 algorithm and a secret key from application properties.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final Long JWT_EXPIRATION_MINUTES_TIME = 10L;
    private static final Long REFRESH_EXPIRATION_DAYS_TIME = 7L;

    /**
     * Generates an access token for the given login with a short expiration time.
     *
     * @param login the username or identifier to embed in the token.
     * @return the generated JWT access token.
     */
    public String generateAccessToken(String login) {
        Date expirationDate = Date.from(
                LocalDateTime
                        .now()
                        .plusMinutes(JWT_EXPIRATION_MINUTES_TIME)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        return Jwts.builder()
                .subject(login)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Generates a refresh token for the given login with a longer expiration time.
     *
     * @param login the username or identifier to embed in the token.
     * @return the generated JWT refresh token.
     */
    public String generateRefreshToken(String login) {
        Date expirationDate = Date.from(
                LocalDateTime
                        .now()
                        .plusDays(REFRESH_EXPIRATION_DAYS_TIME)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        return Jwts.builder()
                .subject(login)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Extracts the subject (login) from a given JWT token.
     *
     * @param token the JWT token.
     * @return the subject (login) embedded in the token.
     */
    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     * Expects the header format: "Bearer &lt;token&gt;".
     *
     * @param request the HTTP servlet request containing the Authorization header.
     * @return the JWT token if present and correctly formatted; {@code null} otherwise.
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Validates the provided JWT token.
     * Checks for proper signature and expiration.
     *
     * @param token the JWT token to validate.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SecurityException e) {
            log.info("JWT validation failed: {}", e.getMessage());
        } catch (Exception e) {
            log.info("Unexpected error during JWT validation: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Returns the signing key used for JWT token generation and validation.
     *
     * @return the {@link SecretKey} derived from the base64-encoded {@code jwt.secret}.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
