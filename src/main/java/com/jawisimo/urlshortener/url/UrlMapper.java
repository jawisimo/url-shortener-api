package com.jawisimo.urlshortener.url;

import com.jawisimo.urlshortener.url.dto.operations.UrlDto;
import com.jawisimo.urlshortener.url.dto.statistics.StatsUrlDto;
import org.springframework.stereotype.Service;

/**
 * Service responsible for mapping URL entities to DTOs in the URL shortener application.
 * <p>
 * This class provides methods to convert a {@link Url} entity to {@link UrlDto} and {@link StatsUrlDto}
 * which are used for various responses in the application.
 * </p>
 */
@Service
public class UrlMapper {

    /**
     * Maps a {@link Url} entity to a {@link UrlDto} for URL operations.
     * <p>
     * This method extracts the relevant data from the {@link Url} entity and constructs a {@link UrlDto} object,
     * which is used for responding to URL-related requests.
     * </p>
     *
     * @param url the URL entity to map
     * @return a {@link UrlDto} representing the URL entity
     */
    public UrlDto mapToUrlDto(Url url) {
        return UrlDto.builder()
                .shortUrlCode(url.getShortUrlCode())
                .longUrl(url.getLongUrl())
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .author(url.getUser().getLogin())
                .build();
    }

    /**
     * Maps a {@link Url} entity to a {@link StatsUrlDto} for statistics purposes.
     * <p>
     * This method maps the {@link Url} entity to a {@link StatsUrlDto}, including additional information about
     * the number of visits and the active status of the URL.
     * </p>
     *
     * @param url the URL entity to map
     * @param isActive the active status of the URL
     * @return a {@link StatsUrlDto} representing the URL entity with statistics
     */
    public StatsUrlDto mapToStatsUrlDto(Url url, boolean isActive) {
        return StatsUrlDto.builder()
                .shortUrlCode(url.getShortUrlCode())
                .longUrl(url.getLongUrl())
                .visits(url.getVisits())
                .isActive(isActive)
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .build();
    }
}
