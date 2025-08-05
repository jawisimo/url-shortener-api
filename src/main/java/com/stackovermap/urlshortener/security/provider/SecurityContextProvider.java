package com.stackovermap.urlshortener.security.provider;

import com.stackovermap.urlshortener.error.exception.UnauthorizedException;
import com.stackovermap.urlshortener.security.CustomUserDetails;
import com.stackovermap.urlshortener.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.stackovermap.urlshortener.util.MessageProvider.UNAUTHORIZED_MESSAGE;

/**
 * Provides access to the currently authenticated {@link User}
 * from the Spring Security context.
 */
@Component
@RequiredArgsConstructor
public class SecurityContextProvider {

    /**
     * Retrieves the currently authenticated {@link User}
     * from the Spring Security context.
     *
     * @return the authenticated user
     * @throws UnauthorizedException if there is no authenticated user in the context
     */
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.user();
    }
}
