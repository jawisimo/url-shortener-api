package com.ikg100.urlshortenerapi.url.controller;

import com.ikg100.urlshortenerapi.openapi.annotations.url.*;
import com.ikg100.urlshortenerapi.url.dto.operations.GetShortUrlRequest;
import com.ikg100.urlshortenerapi.url.dto.operations.UpdateUrlRequest;
import com.ikg100.urlshortenerapi.url.dto.operations.UrlResponse;
import com.ikg100.urlshortenerapi.url.service.UrlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing URL shortening and retrieval in the URL shortener application.
 * <p>
 * Provides endpoints for shortening long URLs, retrieving long URLs from short codes,
 * updating URL mappings, and deleting shortened URLs.
 * </p>
 */
@Tag(name = "2. URL shortener", description = "Endpoints for URL shortening and retrieval")
@RestController
@RequestMapping("/api/v1/url")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    /**
     * Shortens a long URL.
     * <p>
     * This endpoint accepts a long URL and returns a short version of it.
     * </p>
     *
     * @param request the {@link GetShortUrlRequest} containing the long URL and additional data
     * @return a {@link ResponseEntity} containing the short URL code and HTTP status
     */
    @ShortFromLongOpenApi
    @PostMapping
    public ResponseEntity<UrlResponse> shortFromLong(
            @ShortFromLongOpenApiRequestBody
            @RequestBody GetShortUrlRequest request) {
        UrlResponse response = urlService.getShortUrlCodeFromLongUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves the original long URL from a given short URL code.
     * <p>
     * This endpoint takes a short URL code and returns the corresponding long URL.
     * </p>
     *
     * @param shortUrlCode the code of the short URL
     * @return a {@link ResponseEntity} containing the long URL and HTTP status
     */
    @LongFromShortOpenApi
    @PostMapping("/{shortUrlCode}")
    public ResponseEntity<UrlResponse> longFromShort(@PathVariable String shortUrlCode) {
        UrlResponse response = urlService.getLongUrlFromShortUrl(shortUrlCode);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Updates the mapping of a shortened URL.
     * <p>
     * This endpoint allows modification of the existing URL mapping, including changes
     * to the long URL or expiration date.
     * </p>
     *
     * @param request the {@link UpdateUrlRequest} containing updated data for the short URL
     * @return a {@link ResponseEntity} containing the updated URL mapping and HTTP status
     */
    @UpdateUrlOpenApi
    @PatchMapping
    public ResponseEntity<UrlResponse> updateUrl(
            @UpdateUrlOpenApiRequestBody
            @RequestBody UpdateUrlRequest request) {
        UrlResponse response = urlService.updateUrl(request);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Deletes a shortened URL.
     * <p>
     * This endpoint removes a URL mapping from the system based on the provided short URL code.
     * </p>
     *
     * @param shortUrlCode the code of the short URL to be deleted
     * @return a {@link ResponseEntity} with HTTP status 204 (No Content) if deletion is successful
     */
    @DeleteUrlOpenApi
    @DeleteMapping("/{shortUrlCode}")
    public ResponseEntity<UrlResponse> deleteUrl(@PathVariable String shortUrlCode) {
        urlService.deleteUrl(shortUrlCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
