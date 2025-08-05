package com.ikg100.urlshortenerapi.url;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlCodeGeneratorTest {

    private ShortUrlCodeGenerator generator;
    private static final int MIN_SHORT_URL_LENGTH = 6;
    private static final int MAX_SHORT_URL_LENGTH = 8;

    @BeforeEach
    void setUp() {
        generator = new ShortUrlCodeGenerator();
    }

    @Test
    void generateShortUrlCode_shouldReturnNonNullString() {
        String result = generator.generateShortUrlCode();
        assertNotNull(result);
    }

    @Test
    void generateShortUrlCode_shouldReturnString_withValidLength() {
        String result = generator.generateShortUrlCode();
        int resultLength = result.length();
        assertTrue(resultLength >= MIN_SHORT_URL_LENGTH && resultLength <= MAX_SHORT_URL_LENGTH);
    }

    @Test
    void generateShortUrlCode_shouldContainOnlyValidCharacters() {
        String result = generator.generateShortUrlCode();
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    void generateShortUrlCode_shouldGenerateDifferentCodes() {
        String code1 = generator.generateShortUrlCode();
        String code2 = generator.generateShortUrlCode();
        assertNotEquals(code1, code2);
    }
}
