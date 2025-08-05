package com.stackoverblack.urlshortener.url;

import com.stackoverblack.urlshortener.error.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stackoverblack.urlshortener.util.MessageProvider.URL_INCORRECT_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

class LongUrlValidatorTest {

    private LongUrlValidator validator;

    @BeforeEach
    void init() {
        validator = new LongUrlValidator();
    }

    @Test
    void validateLongUrl_shouldPass_whenUrlIsValidHttp() {
        assertDoesNotThrow(() -> validator.validateLongUrl("http://example.com"));
    }

    @Test
    void validateLongUrl_shouldPass_whenUrlIsValidHttps() {
        assertDoesNotThrow(() -> validator.validateLongUrl("https://example.com"));
    }

    @Test
    void validateLongUrl_shouldThrow_whenUrlIsNull() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validateLongUrl(null));
        assertEquals(URL_INCORRECT_MESSAGE, exception.getMessage());
    }

    @Test
    void validateLongUrl_shouldThrow_whenUrlIsEmpty() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validateLongUrl(""));
        assertEquals(URL_INCORRECT_MESSAGE, exception.getMessage());
    }

    @Test
    void validateLongUrl_shouldThrow_whenUrlHasNoProtocol() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validateLongUrl("example.com"));
        assertEquals(URL_INCORRECT_MESSAGE, exception.getMessage());
    }

    @Test
    void validateLongUrl_shouldThrow_whenUrlHasInvalidProtocol() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validateLongUrl("ftp://example.com"));
        assertEquals(URL_INCORRECT_MESSAGE, exception.getMessage());
    }
}
