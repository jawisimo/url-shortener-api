package com.stackovermap.urlshortener.security.service;

import com.stackovermap.urlshortener.security.AuthIdentifierType;
import com.stackovermap.urlshortener.security.CustomUserDetails;
import com.stackovermap.urlshortener.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.stackovermap.urlshortener.util.MessageProvider.*;

/**
 * Custom implementation of {@link UserDetailsService} for Spring Security.
 * <p>
 * Supports authentication by either login or email.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads the user details based on the provided identifier (login or email).
     *
     * @param identifier the login or email of the user
     * @return a {@link CustomUserDetails} object containing user authentication information
     * @throws UsernameNotFoundException if no user is found with the given identifier,
     *                                   or if the identifier type cannot be determined
     */
    @Override
    public CustomUserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        AuthIdentifierType identifierType = AuthIdentifierType.detectType(identifier);

        return switch (identifierType) {
            case AuthIdentifierType.LOGIN -> userRepository.findUserByLogin(identifier)
                    .map(CustomUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            generateUserWithLoginNotFoundMessage(identifier)));
            case AuthIdentifierType.EMAIL -> userRepository.findUserByEmail(identifier)
                    .map(CustomUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            generateUserWithEmailNotFoundMessage(identifier)));
            default -> throw new UsernameNotFoundException(IDENTIFIER_EMPTY_MESSAGE);
        };
    }
}
