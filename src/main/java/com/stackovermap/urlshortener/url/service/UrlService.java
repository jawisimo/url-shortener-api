package com.stackovermap.urlshortener.url.service;

import com.stackovermap.urlshortener.error.exception.ResourceNotFoundException;
import com.stackovermap.urlshortener.error.exception.ValidationException;
import com.stackovermap.urlshortener.security.provider.SecurityContextProvider;
import com.stackovermap.urlshortener.url.*;
import com.stackovermap.urlshortener.url.dto.operations.GetShortUrlRequest;
import com.stackovermap.urlshortener.url.dto.operations.UpdateUrlRequest;
import com.stackovermap.urlshortener.url.dto.operations.UrlDto;
import com.stackovermap.urlshortener.url.dto.operations.UrlResponse;
import com.stackovermap.urlshortener.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.stackovermap.urlshortener.util.MessageProvider.*;

/**
 * Service class for managing URL operations in the URL shortener application.
 * <p>
 * This class handles all URL-related business logic, including generating short URLs from long URLs,
 * retrieving long URLs from short URLs, updating URLs, and deleting URLs.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final SecurityContextProvider contextProvider;
    private final LongUrlValidator urlValidator;
    private final ShortUrlCodeGenerator urlGenerator;
    private final UrlMapper urlMapper;

    /**
     * Generates a short URL code for a given long URL and stores the URL in the repository.
     * <p>
     * This method takes a long URL and generates a unique short URL code. The URL is then stored in the database.
     * If an expiration date is provided, it is validated to ensure it's in the future.
     * </p>
     *
     * @param request the {@link GetShortUrlRequest} containing the long URL and expiration date
     * @return a {@link UrlResponse} containing the generated short URL and its details
     * @throws ValidationException if the expiration date is in the past or if the URL is invalid
     */
    @Transactional
    public UrlResponse getShortUrlCodeFromLongUrl(GetShortUrlRequest request) {
        User user = contextProvider.getAuthenticatedUser();
        String longUrl = request.getLongUrl();
        urlValidator.validateLongUrl(longUrl);
        String shortUrlCode;

        // Generate unique short URL code
        do {
            shortUrlCode = urlGenerator.generateShortUrlCode();
        } while (urlRepository.existsUrlByShortUrlCode(shortUrlCode));

        LocalDateTime expiresAt = request.getExpiresAt();

        if (Objects.nonNull(expiresAt) && LocalDateTime.now().isAfter(expiresAt)) {
            throw new ValidationException(URL_INCORRECT_EXPIRES_AT_MESSAGE);
        }

        // Create and save URL entity
        Url url = Url.builder()
                .shortUrlCode(shortUrlCode)
                .longUrl(longUrl)
                .expiresAt(expiresAt)
                .user(user)
                .build();
        urlRepository.save(url);
        return UrlResponse.createSuccessResponse(urlMapper.mapToUrlDto(url));
    }

    /**
     * Retrieves the long URL corresponding to the given short URL code.
     * <p>
     * This method checks if the short URL exists in the database and if it is still valid (not expired).
     * The long URL is returned if the short URL is found and valid.
     * </p>
     *
     * @param shortUrlCode the short URL code
     * @return a {@link UrlResponse} containing the long URL
     * @throws ResourceNotFoundException if the short URL code does not exist
     * @throws ValidationException if the short URL has expired
     */
    @Transactional
    public UrlResponse getLongUrlFromShortUrl(String shortUrlCode) {
        if (Objects.isNull(shortUrlCode) || shortUrlCode.isEmpty()) {
            throw new ValidationException(URL_INCORRECT_MESSAGE);
        }

        Url url = urlRepository.findUrlByShortUrlCode(shortUrlCode)
                .orElseThrow(() -> new ResourceNotFoundException(URL_NOT_FOUND_MESSAGE));

        LocalDateTime expiresAt = url.getExpiresAt();

        // Check if the URL is expired
        if (Objects.nonNull(expiresAt) && expiresAt.isBefore(LocalDateTime.now())) {
            throw new ValidationException(URL_EXPIRED_MESSAGE);
        }

        // Increment the visit count and save the URL
        url.setVisits(url.getVisits() + 1);
        urlRepository.save(url);
        UrlDto urlDto = UrlDto.builder().longUrl(url.getLongUrl()).build();
        return UrlResponse.createSuccessResponse(urlDto);
    }

    /**
     * Updates the expiration date and/or short URL code of an existing URL.
     * <p>
     * This method allows the user to update an existing URL's short URL code and expiration date.
     * The URL must belong to the authenticated user, and the expiration date must be valid.
     * </p>
     *
     * @param request the {@link UpdateUrlRequest} containing the new short URL code and expiration date
     * @return a {@link UrlResponse} containing the updated URL details
     * @throws ResourceNotFoundException if the URL does not exist or does not belong to the user
     * @throws ValidationException if the expiration date is in the past
     */
    @Transactional
    public UrlResponse updateUrl(UpdateUrlRequest request) {
        User user = contextProvider.getAuthenticatedUser();

        Url url = urlRepository.findUrlByShortUrlCode(request.getShortUrlCode())
                .filter(u -> u.getUser().getLogin().equals(user.getLogin()))
                .orElseThrow(() -> new ResourceNotFoundException(URL_NOT_FOUND_MESSAGE));

        LocalDateTime expiresAt = request.getExpiresAt();

        if (Objects.nonNull(expiresAt) && LocalDateTime.now().isAfter(expiresAt)) {
            throw new ValidationException(URL_INCORRECT_EXPIRES_AT_MESSAGE);
        }

        // Update URL properties
        String newShortUrlCode = urlGenerator.generateShortUrlCode();
        url.setShortUrlCode(newShortUrlCode);

        // Here we have to check the date from the request
        // so as not to set the value to an existing date
        if (Objects.nonNull(expiresAt)) {
            url.setExpiresAt(expiresAt);
        }

        urlRepository.save(url);
        return UrlResponse.createSuccessResponse(urlMapper.mapToUrlDto(url));
    }

    /**
     * Deletes a URL by its short URL code.
     * <p>
     * This method deletes the URL associated with the given short URL code.
     * The URL must belong to the authenticated user.
     * </p>
     *
     * @param shortUrlCode the short URL code of the URL to delete
     * @throws ResourceNotFoundException if the URL does not exist or does not belong to the user
     */
    @Transactional
    public void deleteUrl(String shortUrlCode) {
        User user = contextProvider.getAuthenticatedUser();
        Url url = urlRepository.findUrlByShortUrlCode(shortUrlCode)
                .filter(u -> u.getUser().getLogin().equals(user.getLogin()))
                .orElseThrow(() -> new ResourceNotFoundException(URL_NOT_FOUND_MESSAGE));
        urlRepository.delete(url);
    }
}
