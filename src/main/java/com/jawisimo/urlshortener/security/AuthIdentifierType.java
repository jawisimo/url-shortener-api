package com.jawisimo.urlshortener.security;

import java.util.Objects;

/**
 * Represents the type of authentication identifier.
 * It can be a login name, an email, or empty.
 */
public enum AuthIdentifierType {

    /** Username-based identifier */
    LOGIN,

    /** Email-based identifier */
    EMAIL,

    /** Empty or null identifier */
    EMPTY;

    /**
     * Detects the type of the given identifier string.
     *
     * @param identifier the input string to analyze
     * @return the corresponding AuthIdentifierType
     */
    public static AuthIdentifierType detectType(String identifier) {
        if (Objects.isNull(identifier) || identifier.isEmpty()) {
            return EMPTY;
        } else if (identifier.contains("@")) {
            return EMAIL;
        } else {
            return LOGIN;
        }
    }
}
