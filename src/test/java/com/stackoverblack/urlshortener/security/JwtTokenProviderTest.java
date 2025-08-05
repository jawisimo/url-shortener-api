package com.stackoverblack.urlshortener.security;

import com.stackoverblack.urlshortener.security.provider.JwtTokenProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;
    private String login;
    private int minSizeJwtToken;

    @BeforeAll
    void init() {
        login = "testUser123";
        minSizeJwtToken = 50;
    }

    @Test
    void generateAccessToken_shouldReturnAccessToken_whenLoginIsValid() {
        String token = tokenProvider.generateAccessToken(login);
        assertNotNull(token);
        assertTrue(token.length() >= minSizeJwtToken);
    }

    @Test
    void generateRefreshToken_shouldReturnRefreshToken_whenLoginIsValid() {
        String token = tokenProvider.generateRefreshToken(login);
        assertNotNull(token);
        assertTrue(token.length() >= minSizeJwtToken);
    }

    @Test
    void getLoginFromToken_shouldReturnLogin() {
        String token = tokenProvider.generateAccessToken(login);
        String extractedLogin = tokenProvider.getLoginFromToken(token);
        String tokenWithNullLogin = tokenProvider.generateAccessToken(null);
        assertNotNull(token);
        assertNotNull(extractedLogin);
        assertEquals(login, extractedLogin);
        assertNull(tokenProvider.getLoginFromToken(tokenWithNullLogin));
    }

    @Test
    void validateToken_shouldReturnTrue_whenTokenIsValid() {
        String token = tokenProvider.generateAccessToken(login);
        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalse_whenTokenIsNotValid() {
        String notValidToken = "jjhdoioiIIihks";
        assertFalse(tokenProvider.validateToken(notValidToken));
    }
}
