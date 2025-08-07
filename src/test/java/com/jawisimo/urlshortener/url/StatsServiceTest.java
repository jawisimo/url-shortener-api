package com.jawisimo.urlshortener.url;

import com.jawisimo.urlshortener.error.exception.ResourceNotFoundException;
import com.jawisimo.urlshortener.security.provider.SecurityContextProvider;
import com.jawisimo.urlshortener.url.dto.statistics.StatsListUrlResponse;
import com.jawisimo.urlshortener.url.dto.statistics.StatsUrlDto;
import com.jawisimo.urlshortener.url.dto.statistics.StatsVisitsUrlResponse;
import com.jawisimo.urlshortener.url.service.StatsService;
import com.jawisimo.urlshortener.user.User;
import com.jawisimo.urlshortener.util.MessageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private SecurityContextProvider contextProvider;

    @Mock
    private UrlMapper urlMapper;

    @InjectMocks
    private StatsService statsService;

    private User user;
    private Url activeUrl;
    private Url expiredUrl;
    private StatsUrlDto activeStatsUrlDto;
    private StatsUrlDto expiredStatsUrlDto;
    private String shortUrlCode;


    @BeforeEach
    void init() {
        user = User.builder().id(1L).login("testUser").build();
        shortUrlCode = "abc123";
        activeUrl = Url.builder()
                .shortUrlCode(shortUrlCode)
                .longUrl("https://example.com")
                .user(user)
                .visits(5L)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
        expiredUrl = Url.builder()
                .shortUrlCode("xyz789")
                .longUrl("https://expired.com")
                .user(user)
                .visits(3L)
                .expiresAt(LocalDateTime.now().minusDays(1))
                .build();
        activeStatsUrlDto = StatsUrlDto.builder()
                .shortUrlCode(shortUrlCode)
                .longUrl("https://example.com")
                .visits(5L)
                .isActive(true)
                .build();
        expiredStatsUrlDto = StatsUrlDto.builder()
                .shortUrlCode("xyz789")
                .longUrl("https://expired.com")
                .visits(3L)
                .isActive(false)
                .build();
    }

    @Test
    void getAllUrlsByUser_shouldReturnSuccessResponse_withAllUrls() {
        List<Url> urls = List.of(activeUrl, expiredUrl);
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findAllUrlsByUserId(user.getId())).thenReturn(urls);
        when(urlMapper.mapToStatsUrlDto(activeUrl, true)).thenReturn(activeStatsUrlDto);
        when(urlMapper.mapToStatsUrlDto(expiredUrl, false)).thenReturn(expiredStatsUrlDto);

        StatsListUrlResponse response = statsService.getAllUrls();

        assertNotNull(response);
        assertEquals(8L, response.getTotalVisits());
        assertEquals(2, response.getUrls().size());
        assertTrue(response.getUrls().contains(activeStatsUrlDto));
        assertTrue(response.getUrls().contains(expiredStatsUrlDto));
    }

    @Test
    void getAllUrlsByUser_shouldReturnEmptyResponse_whenNoUrlsExist() {
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findAllUrlsByUserId(user.getId())).thenReturn(Collections.emptyList());

        StatsListUrlResponse response = statsService.getAllUrls();

        assertNotNull(response);
        assertEquals(0L, response.getTotalVisits());
        assertTrue(response.getUrls().isEmpty());
    }

    @Test
    void getActiveUrlsByUser_shouldReturnSuccessResponse_withActiveUrlsOnly() {
        List<Url> urls = List.of(activeUrl, expiredUrl);
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findAllUrlsByUserId(user.getId())).thenReturn(urls);
        when(urlMapper.mapToStatsUrlDto(activeUrl, true)).thenReturn(activeStatsUrlDto);

        StatsListUrlResponse response = statsService.getActiveUrls();

        assertNotNull(response);
        assertEquals(5L, response.getTotalVisits());
        assertEquals(1, response.getUrls().size());
        assertTrue(response.getUrls().contains(activeStatsUrlDto));
        assertFalse(response.getUrls().contains(expiredStatsUrlDto));
    }

    @Test
    void getActiveUrlsByUser_shouldReturnEmptyResponse_whenNoActiveUrlsExist() {
        List<Url> urls = List.of(expiredUrl);
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findAllUrlsByUserId(user.getId())).thenReturn(urls);

        StatsListUrlResponse response = statsService.getActiveUrls();

        assertNotNull(response);
        assertEquals(0L, response.getTotalVisits());
        assertTrue(response.getUrls().isEmpty());
    }

    @Test
    void getVisitsByShortUrl_shouldReturnSuccessResponse_whenUrlExistsAndUserIsOwner() {
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findUrlByShortUrlCode("abc123")).thenReturn(Optional.of(activeUrl));

        StatsVisitsUrlResponse response = statsService.getVisitsByShortUrl("abc123");

        assertNotNull(response);
        assertEquals(5L, response.getVisits());
    }

    @Test
    void getVisitsByShortUrl_shouldThrowResourceNotFound_whenUrlNotFound() {
        when(contextProvider.getAuthenticatedUser()).thenReturn(user);
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> statsService.getVisitsByShortUrl(shortUrlCode));
        assertEquals(MessageProvider.URL_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void getVisitsByShortUrl_shouldThrowResourceNotFound_whenUserIsNotOwner() {
        User anotherUser = User.builder().id(2L).login("anotherUser").build();
        when(contextProvider.getAuthenticatedUser()).thenReturn(anotherUser);
        when(urlRepository.findUrlByShortUrlCode(shortUrlCode)).thenReturn(Optional.of(activeUrl));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> statsService.getVisitsByShortUrl(shortUrlCode));
        assertEquals(MessageProvider.URL_NOT_FOUND_MESSAGE, exception.getMessage());
    }
}
