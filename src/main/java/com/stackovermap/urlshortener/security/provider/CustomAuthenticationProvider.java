package com.stackovermap.urlshortener.security.provider;

import com.stackovermap.urlshortener.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.stackovermap.urlshortener.util.MessageProvider.PASSWORD_WRONG_MESSAGE;


/**
 * Custom authentication provider that validates user credentials
 * by loading user details from {@link CustomUserDetailsService} and
 * checking the password using {@link PasswordEncoder}.
 * Implements the {@link AuthenticationProvider} interface.
 */
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates the provided {@link Authentication} object by verifying
     * the username and password.
     *
     * @param authentication the {@link Authentication} object containing user credentials.
     * @return a fully populated {@link Authentication} token if authentication is successful.
     * @throws AuthenticationException if authentication fails due to invalid credentials.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identifier = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails;

        try {
            userDetails = userDetailsService.loadUserByUsername(identifier);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(PASSWORD_WRONG_MESSAGE);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    /**
     * Verifies if this provider supports the given {@link Authentication} type.
     *
     * @param authentication the {@link Authentication} class to check.
     * @return {@code true} if the provider supports {@link UsernamePasswordAuthenticationToken},
     *         otherwise {@code false}.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
