package com.ikg100.urlshortenerapi.user;

import com.ikg100.urlshortenerapi.security.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String login;
    private String email;
    private String notExistsLogin;
    private String notExistsEmail;
    private User expectedUser;

    @BeforeAll
    void setUp() {
        login = "testUser";
        email = "test@email.com";
        notExistsLogin = login + 2;
        notExistsEmail = email + 2;
        String password = "hashPassWord123";
        expectedUser = User.builder()
                .id(1L)
                .login(login)
                .email(email)
                .password(password)
                .role(Role.ROLE_USER)
                .build();
        User user = User.builder()
                .login(login)
                .email(email)
                .password(password)
                .role(Role.ROLE_USER)
                .build();
        clearDb();
        userRepository.save(user);
    }

    @AfterAll
    void cleanUp() {
        clearDb();
    }

    @Test
    void findUserByLogin_shouldReturnUser_whenUserExists() {
        User foundUser = userRepository.findUserByLogin(login).orElse(null);
        assertNotNull(foundUser);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    void findUserByLogin_shouldReturnNull_whenUserDoesNotExist() {
        Optional<User> foundUserOptional = userRepository.findUserByLogin(notExistsLogin);
        assertTrue(foundUserOptional.isEmpty());
    }


    @Test
    void findUserByEmail_shouldReturnUser_whenUserExists() {
        User foundUser = userRepository.findUserByEmail(email).orElse(null);
        assertNotNull(foundUser);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    void findUserByEmail_shouldReturnNull_whenUserDoesNotExist() {
        Optional<User> foundUserOptional = userRepository.findUserByEmail(notExistsEmail);
        assertTrue(foundUserOptional.isEmpty());
    }

    @Test
    void findUserByLoginOrEmail_shouldReturnUser_whenLoginExists() {
        User foundUser = userRepository.findUserByLoginOrEmail(login, notExistsEmail).orElse(null);
        assertNotNull(foundUser);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    void findUserByLoginOrEmail_shouldReturnUser_whenEmailExists() {
        User foundUser = userRepository.findUserByLoginOrEmail(notExistsLogin, email).orElse(null);
        assertNotNull(foundUser);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    void findUserByLoginOrEmail_shouldReturnNull_whenUserDoesNotExist() {
        Optional<User> foundUserOptional = userRepository.findUserByLoginOrEmail(notExistsLogin, notExistsEmail);
        assertTrue(foundUserOptional.isEmpty());
    }

    private void clearDb() {
        userRepository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE users_id_seq RESTART WITH 1");
    }
}
