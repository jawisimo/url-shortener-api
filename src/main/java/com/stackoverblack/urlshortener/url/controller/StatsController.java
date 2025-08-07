package com.stackoverblack.urlshortener.url.controller;

import com.stackoverblack.urlshortener.doc.annotation.url.UrlListOpenApi;
import com.stackoverblack.urlshortener.doc.annotation.url.VisitsByShortUrlOpenApi;
import com.stackoverblack.urlshortener.url.dto.statistics.StatsListUrlResponse;
import com.stackoverblack.urlshortener.url.dto.statistics.StatsVisitsUrlResponse;
import com.stackoverblack.urlshortener.url.service.StatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing URL statistics in the URL shortener application.
 * <p>
 * Provides endpoints for retrieving statistics about URLs, including retrieving
 * all URLs, active URLs, and visit counts for specific short URLs.
 * </p>
 */
@Tag(name = "3. URL statistics", description = "Endpoints for retrieving statistics about URLs")
@RestController
@RequestMapping("/api/v1/url")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    /**
     * Retrieves all URLs created by the authenticated user.
     * <p>
     * This endpoint fetches the list of all URLs associated with the authenticated
     * user. The response contains the details of each URL.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a list of URLs and an HTTP status
     */
    @UrlListOpenApi
    @GetMapping("/all")
    public ResponseEntity<StatsListUrlResponse> allUrls() {
        StatsListUrlResponse response = statsService.getAllUrls();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves all active (non-expired) URLs for the user.
     * <p>
     * This endpoint returns a list of URLs that are still active (i.e., not expired)
     * and belong to the authenticated user.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a list of active URLs and an HTTP status
     */
    @UrlListOpenApi
    @GetMapping("/active")
    public ResponseEntity<StatsListUrlResponse> activeUrls() {
        StatsListUrlResponse response = statsService.getActiveUrls();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves the visit count for a specific short URL.
     * <p>
     * This endpoint fetches the number of visits for a given short URL identified by
     * its code.
     * </p>
     *
     * @param shortUrlCode the code of the short URL whose visit count is to be retrieved
     * @return a {@link ResponseEntity} containing the visit count for the specified URL
     */
    @VisitsByShortUrlOpenApi
    @GetMapping("/visits/{shortUrlCode}")
    public ResponseEntity<StatsVisitsUrlResponse> visitsByShortUrl(@PathVariable String shortUrlCode) {
        StatsVisitsUrlResponse response = statsService.getVisitsByShortUrl(shortUrlCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
