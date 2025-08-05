package com.stackovermap.urlshortener.url;

import com.stackovermap.urlshortener.security.Role;
import com.stackovermap.urlshortener.user.User;
import com.stackovermap.urlshortener.user.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UrlRepositoryIntegrationTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    private String shortUrlCode;
    private String notExistShortUrlCode;
    private String longUrl;
    private String login;
    private User user;

    @BeforeAll
    void setUp() {
        shortUrlCode = "XjlOguI";
        notExistShortUrlCode = shortUrlCode + 1;
        longUrl = "https://www.youtube.com/";
        login = "login123";
        String email = "test@email.com";
        String password = "passWord123";
        user = User.builder()
                .login(login)
                .email(email)
                .password(password)
                .role(Role.ROLE_USER)
                .build();
        clearDb();
        userRepository.save(user);
        Url url = Url.builder()
                .shortUrlCode(shortUrlCode)
                .longUrl(longUrl)
                .user(user)
                .build();
        urlRepository.save(url);
    }

    @AfterAll
    void cleanUp() {
        clearDb();
    }

    @Test
    void findUrlByShortUrlCode_shouldReturnUrl_whenShortUrlCodeExists() {
        Url foundUrl = urlRepository.findUrlByShortUrlCode(shortUrlCode).orElse(null);
        assertNotNull(foundUrl);
        assertEquals(shortUrlCode, foundUrl.getShortUrlCode());
        assertEquals(longUrl, foundUrl.getLongUrl());
        assertEquals(login, foundUrl.getUser().getLogin());
    }

    @Test
    void findUrlByShortUrlCode_shouldReturnNull_whenShortUrlCodeDoesNotExist() {
        Optional<Url> foundUrl = urlRepository.findUrlByShortUrlCode(notExistShortUrlCode);
        assertTrue(foundUrl.isEmpty());
    }

    @Test
    void findAllUrlsByUser_shouldReturnAllUrls_whenUrlsExists() {
        List<Url> urls = urlRepository.findAllUrlsByUserId(user.getId());
        assertNotNull(urls);
        assertEquals(1, urls.size());
        assertEquals(shortUrlCode, urls.getFirst().getShortUrlCode());
        assertEquals(longUrl, urls.getFirst().getLongUrl());
    }

    @Test
    void existsUrlByShortUrlCode_shouldReturnTrue_whenShortUrlCodeExists() {
        assertTrue(urlRepository.existsUrlByShortUrlCode(shortUrlCode));
    }

    @Test
    void existsUrlByShortUrlCode_shouldReturnFalse_whenUrlDoesNotExist() {
        assertFalse(urlRepository.existsUrlByShortUrlCode(notExistShortUrlCode));
    }

    private void clearDb() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
    }
}
