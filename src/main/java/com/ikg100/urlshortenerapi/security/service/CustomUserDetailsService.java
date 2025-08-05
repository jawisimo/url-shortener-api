package com.ikg100.urlshortenerapi.security.service;

import com.ikg100.urlshortenerapi.security.AuthIdentifierType;
import com.ikg100.urlshortenerapi.security.CustomUserDetails;
import com.ikg100.urlshortenerapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.ikg100.urlshortenerapi.util.MessageProvider.*;

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
