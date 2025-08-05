package com.stackoverblack.urlshortener.url;

import com.stackoverblack.urlshortener.error.exception.ValidationException;
import org.springframework.stereotype.Component;

import static com.stackoverblack.urlshortener.util.MessageProvider.URL_INCORRECT_MESSAGE;

/**
 * Component responsible for validating long URLs in the URL shortener application.
 * <p>
 * This class ensures that the provided long URL is not null, empty, and matches the required format (http or https).
 * </p>
 */
@Component
public class LongUrlValidator {

    private static final String URL_REGEX = "^(http://|https://).+";

    /**
     * Validates the provided long URL.
     * <p>
     * This method checks that the URL is not null or empty, and that it starts with either "http://" or "https://".
     * </p>
     *
     * @param longUrl the long URL to validate
     * @throws ValidationException if the URL is invalid
     */
    public void validateLongUrl(String longUrl) {
        if (longUrl == null || longUrl.isEmpty() || !longUrl.matches(URL_REGEX)) {
            throw new ValidationException(URL_INCORRECT_MESSAGE);
        }
    }
}
