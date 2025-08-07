package com.jawisimo.urlshortener.security;

import com.jawisimo.urlshortener.security.provider.JwtTokenProvider;
import com.jawisimo.urlshortener.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authorization Filter that processes incoming HTTP requests to check the validity of JWT tokens.
 * <p>
 * If a valid JWT token is present, this filter extracts the login information from the token,
 * loads the corresponding user details, and sets them in the Spring Security context.
 * </p>
 * This class extends {@link OncePerRequestFilter} to ensure that the filter is applied once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtService;
    private final CustomUserDetailsService detailService;

    /**
     * Filters the incoming HTTP request to check for the presence and validity of a JWT token.
     * <p>
     * If a valid JWT token is found, this method extracts the login information from the token
     * and sets the user details into the Spring Security context using {@link CustomUserDetails}.
     * </p>
     *
     * @param request The HTTP request that may contain an Authorization header with the JWT token.
     * @param response The HTTP response to be sent after filtering the request.
     * @param filterChain The filter chain to continue processing the request after this filter.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException If an I/O error occurs during the filtering process.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtService.getTokenFromRequest(request);

        if (jwtService.validateToken(token)) {
            setCustomUserDetailsToSecurityContextHolder(token);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Sets the {@link CustomUserDetails} to the {@link SecurityContextHolder} using the login information
     * extracted from the JWT token.
     *
     * @param token The JWT token from which the login information will be extracted.
     */
    private void setCustomUserDetailsToSecurityContextHolder(String token) {
        String login = jwtService.getLoginFromToken(token);
        CustomUserDetails customUserDetails = detailService.loadUserByUsername(login);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
