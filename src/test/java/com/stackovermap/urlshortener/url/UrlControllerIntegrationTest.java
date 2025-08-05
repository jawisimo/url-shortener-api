package com.stackovermap.urlshortener.url;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackovermap.urlshortener.security.Role;
import com.stackovermap.urlshortener.url.dto.operations.GetShortUrlRequest;
import com.stackovermap.urlshortener.url.dto.operations.UpdateUrlRequest;
import com.stackovermap.urlshortener.user.User;
import com.stackovermap.urlshortener.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.stackovermap.urlshortener.util.MessageProvider.URL_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UrlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UrlRepository urlRepository;

    private static final String URL_PATH = "/api/v1/url";
    private String shortUrlCode;
    private String longUrl;
    private String notExistsShortUrlCode;
    private String incorrectLongUrl;
    private LocalDateTime expiresAt;
    private String login;
    private User user;

    @BeforeAll
    void setup() {
        shortUrlCode = "dj5Kdt";
        longUrl = "https://www.youtube.com";
        incorrectLongUrl = "youtube";
        notExistsShortUrlCode = shortUrlCode + 1;
        login = "testUser";
        expiresAt = ZonedDateTime.parse("2028-08-30T00:00:00.000Z").toLocalDateTime();
        user = User.builder()
                .login("testUser")
                .email("test@email.com")
                .password("passWord123")
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
    }

    @BeforeEach
    void saveTestUrl() {
        Url url = Url.builder()
                .shortUrlCode(shortUrlCode)
                .longUrl(longUrl)
                .expiresAt(expiresAt)
                .user(user)
                .build();
        urlRepository.save(url);
    }

    @AfterEach
    void deleteTestUrl() {
        urlRepository.deleteAll();
    }

    @AfterAll
    void cleanUp() {
        clearDb();
    }

    @Test
    @WithUserDetails("testUser")
    void shortFromLong_shouldReturnCreatedAndResponseBody_withSuccessRequest() throws Exception {
        GetShortUrlRequest request = new GetShortUrlRequest(longUrl, expiresAt);
        mockMvc.perform(post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(longUrl)))
                .andExpect(content().string(containsString(login)));
    }

    @Test
    @WithUserDetails("testUser")
    void shortFromLong_shouldReturnBadRequestAndResponseBody_withIncorrectLongUrl() throws Exception {
        GetShortUrlRequest request = new GetShortUrlRequest(incorrectLongUrl);
        mockMvc.perform(post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void shortFromLong_shouldReturnForbidden_withAnonymousUser() throws Exception {
        GetShortUrlRequest requestSuccess = new GetShortUrlRequest(longUrl, expiresAt);
        mockMvc.perform(post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSuccess)))
                .andExpect(status().isForbidden());
    }

    @Test
    void longFromShort_shouldReturnOkAndResponseBody_withSuccessRequest() throws Exception {
        mockMvc.perform(post(URL_PATH + "/" + shortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(longUrl)));
    }

    @Test
    void longFromShort_shouldReturnBadRequestAndResponseBody_whenUrlDoesNotExists() throws Exception {
        mockMvc.perform(post(URL_PATH + "/" + notExistsShortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(URL_NOT_FOUND_MESSAGE)));
    }

    @Test
    @WithUserDetails("testUser")
    void updateUrl_shouldReturnOkAndResponseBody_withSuccessRequest() throws Exception {
        UpdateUrlRequest request = new UpdateUrlRequest(shortUrlCode, expiresAt);
        mockMvc.perform(patch(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(longUrl)))
                .andExpect(content().string(containsString(login)));
    }

    @Test
    @WithUserDetails("testUser")
    void updateUrl_shouldReturnNotFound_whenUrlIsNotExists() throws Exception {
        UpdateUrlRequest request = new UpdateUrlRequest(notExistsShortUrlCode);
        mockMvc.perform(patch(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(URL_NOT_FOUND_MESSAGE)));
    }

    @Test
    @WithAnonymousUser
    void updateUrl_shouldReturnForbidden_withAnonymousUser() throws Exception {
        UpdateUrlRequest request = new UpdateUrlRequest(shortUrlCode, expiresAt);
        mockMvc.perform(patch(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("testUser")
    void deleteUrl_shouldReturnNoContent_withSuccessRequest() throws Exception {
        mockMvc.perform(delete(URL_PATH + "/" + shortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("testUser")
    void deleteUrl_shouldReturnNotFoundAndResponseBody_whenUrlDoesNotExists() throws Exception {
        mockMvc.perform(delete(URL_PATH + "/" + notExistsShortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(URL_NOT_FOUND_MESSAGE)));
    }


    @Test
    @WithAnonymousUser
    void deleteUrl_shouldReturnForbidden_withAnonymousUser() throws Exception {
        mockMvc.perform(delete(URL_PATH + "/" + shortUrlCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void clearDb() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
    }
}
