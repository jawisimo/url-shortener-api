package com.jawisimo.urlshortener.url;

import com.jawisimo.urlshortener.error.exception.ResourceNotFoundException;
import com.jawisimo.urlshortener.error.exception.ValidationException;
import com.jawisimo.urlshortener.security.provider.SecurityContextProvider;
import com.jawisimo.urlshortener.url.dto.operations.GetShortUrlRequest;
import com.jawisimo.urlshortener.url.dto.operations.UpdateUrlRequest;
import com.jawisimo.urlshortener.url.dto.operations.UrlDto;
import com.jawisimo.urlshortener.url.dto.operations.UrlResponse;
import com.jawisimo.urlshortener.url.service.UrlService;
import com.jawisimo.urlshortener.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.jawisimo.urlshortener.util.MessageProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private SecurityContextProvider contextProvider;

    @Spy
    private LongUrlValidator urlValidator; // used by @InjectMocks to inject into UrlService

    @Mock
    private ShortUrlCodeGenerator urlGenerator;

    @Mock
    private UrlMapper urlMapper;

    @InjectMocks
    private UrlService urlService;

    private User user;
    private Url url;
    private UrlDto urlDto;
    private String shortUrlCode;
    private String longUrl;

    @BeforeEach
    void init() {
        user = User.builder().id(1L).login("testUser").build();
        shortUrlCode = "abc123";
        longUrl = "https://example.com";
        url = Url.builder()
                .shortUrlCode(shortUrlCode)
                .longUrl(longUrl)
                .user(user)
                .build();
        urlDto = UrlDto.builder().longUrl(longUrl).build();
    }

    @Test
    void getShortUrlCodeFromLongUrl_shouldReturnSuccessResponse_whenRequestIsValid() {
        GetShortUrlRequest request = new GetShortUrlRequest(longUrl, null);
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlGenerator.generateShortUrlCode()).thenReturn(shortUrlCode);
        when(urlRepository.existsUrlByShortUrlCode(shortUrlCode)).thenReturn(false);
        when(urlMapper.mapToUrlDto(any(Url.class))).thenReturn(urlDto);

        UrlResponse response = urlService.getShortUrlCodeFromLongUrl(request);

        assertNotNull(response);
        assertEquals(urlDto, response.getUrlDto());
        verify(urlRepository, times(1)).save(any(Url.class));
    }

    @Test
    void getShortUrlCodeFromLongUrl_shouldThrowValidationException_whenExpiresAtIsInPast() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        GetShortUrlRequest request = new GetShortUrlRequest(longUrl, pastDate);
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> urlService.getShortUrlCodeFromLongUrl(request));
        assertEquals(URL_INCORRECT_EXPIRES_AT_MESSAGE, exception.getMessage());
    }

    @Test
    void getLongUrlFromShortUrl_shouldThrowResourceNotFound_whenUrlNotFound() {
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> urlService.getLongUrlFromShortUrl(shortUrlCode)
        );

        assertTrue(exception.getMessage().contains("URL not found"));
    }

    @Test
    void getLongUrlFromShortUrl_shouldThrowValidationException_whenUrlExpired() {
        url.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.of(url));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> urlService.getLongUrlFromShortUrl(shortUrlCode));
        assertEquals(URL_EXPIRED_MESSAGE, exception.getMessage());
    }

    @Test
    void getLongUrlFromShortUrl_shouldThrowValidationException_whenShortUrlCodeIsNull() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> urlService.getLongUrlFromShortUrl(null));
        assertEquals(URL_INCORRECT_MESSAGE, exception.getMessage());
    }

    @Test
    void updateUrl_shouldReturnSuccessResponse_whenUrlExistsAndUserIsOwner() {
        UpdateUrlRequest request = new UpdateUrlRequest(shortUrlCode, LocalDateTime.now().plusDays(1));
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.of(url));
        when(urlGenerator.generateShortUrlCode()).thenReturn("newCode");
        when(urlMapper.mapToUrlDto(any(Url.class))).thenReturn(urlDto);

        UrlResponse response = urlService.updateUrl(request);

        assertNotNull(response);
        assertEquals(urlDto, response.getUrlDto());
        assertEquals("newCode", url.getShortUrlCode());
        verify(urlRepository, times(1)).save(url);
    }

    @Test
    void updateUrl_shouldThrowResourceNotFound_whenUrlNotFoundOrUserNotOwner() {
        UpdateUrlRequest request = new UpdateUrlRequest(shortUrlCode, null);
        User anotherUser = User.builder().id(2L).build();
        when(contextProvider.getAuthenticatedUser()).thenReturn(anotherUser);
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.of(url));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> urlService.updateUrl(request));
        assertEquals(URL_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void updateUrl_shouldThrowValidationException_whenExpiresAtIsInPast() {
        UpdateUrlRequest request = new UpdateUrlRequest(shortUrlCode, LocalDateTime.now().minusDays(1));
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.of(url));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> urlService.updateUrl(request));
        assertEquals(URL_INCORRECT_EXPIRES_AT_MESSAGE, exception.getMessage());
    }

    @Test
    void deleteUrl_shouldDeleteUrl_whenUrlExistsAndUserIsOwner() {
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.of(url));

        urlService.deleteUrl(shortUrlCode);

        verify(urlRepository, times(1)).delete(url);
    }

    @Test
    void deleteUrl_shouldThrowResourceNotFound_whenUrlNotFoundOrUserNotOwner() {
        User anotherUser = User.builder().id(2L).build();
        when(contextProvider.getAuthenticatedUser()).thenReturn(anotherUser);
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.of(url));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> urlService.deleteUrl(shortUrlCode));
        assertEquals(URL_NOT_FOUND_MESSAGE, exception.getMessage());
    }
}
