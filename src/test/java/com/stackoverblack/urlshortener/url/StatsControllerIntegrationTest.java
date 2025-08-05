package com.stackoverblack.urlshortener.url;

import com.stackoverblack.urlshortener.security.Role;
import com.stackoverblack.urlshortener.user.User;
import com.stackoverblack.urlshortener.user.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.stackoverblack.urlshortener.util.MessageProvider.URL_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UrlRepository urlRepository;

    private static final String URL_PATH = "/api/v1/url";
    private String shortUrlCode;
    private String shortUrlCodeNotExists;
    private int visits;


    @BeforeAll
    void setUp() {
        shortUrlCode = "dj5Kdt";
        String longUrl = "https://www.youtube.com";
        shortUrlCodeNotExists = shortUrlCode + 1;
        visits = 100;
        LocalDateTime expiresAt = ZonedDateTime.parse("2028-08-30T00:00:00.000Z").toLocalDateTime();
        User user = User.builder()
                .login("testUser")
                .email("test@email")
                .password("passWord123")
                .role(Role.ROLE_USER)
                .build();
        clearDb();
        userRepository.save(user);
        Url url = Url.builder()
                .shortUrlCode(shortUrlCode)
                .longUrl(longUrl)
                .expiresAt(expiresAt)
                .user(user)
                .build();
        urlRepository.save(url);
    }

    @AfterAll
    void cleanUp() {
        clearDb();
    }

    @Test
    @WithUserDetails("testUser")
    void allUrls_shouldReturnOkAndResponseBody_withSuccessRequest() throws Exception {
        mockMvc.perform(get(URL_PATH + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(shortUrlCode)));
    }

    @Test
    @WithAnonymousUser
    void allUrls_shouldReturnForbidden_withAnonymousUser() throws Exception {
        mockMvc.perform(get(URL_PATH + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("testUser")
    void activeUrls_shouldReturnOkAndResponseBody_withSuccessRequest() throws Exception {
        mockMvc.perform(get(URL_PATH + "/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(shortUrlCode)));
    }

    @Test
    @WithAnonymousUser
    void activeUrls_shouldReturnForbidden_withAnonymousUser() throws Exception {
        mockMvc.perform(get(URL_PATH + "/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("testUser")
    void visitsByShortUrl_shouldReturnOkAndResponseBody_withSuccessRequest() throws Exception {
        //Visits
        for (int i = 1; i <= visits; i++) {
            mockMvc.perform(post(URL_PATH + "/" + shortUrlCode));
        }

        //Success after visit
        mockMvc.perform(get(URL_PATH + "/visits/" + shortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(visits))));
    }

    @Test
    @WithUserDetails("testUser")
    void visitsByShortUrl_shouldReturnNotFound_whenUrlDoesNotExists() throws Exception {
        mockMvc.perform(get(URL_PATH + "/visits/" + shortUrlCodeNotExists)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(URL_NOT_FOUND_MESSAGE)));
    }

    @Test
    @WithAnonymousUser
    void visitsByShortUrl_shouldReturnForbidden_withAnonymousUser() throws Exception {
        mockMvc.perform(get(URL_PATH + "/visits/" + shortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    private void clearDb() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
    }
}
