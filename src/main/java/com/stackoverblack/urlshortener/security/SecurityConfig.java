package com.stackoverblack.urlshortener.security;

import com.stackoverblack.urlshortener.security.provider.CustomAuthenticationProvider;
import com.stackoverblack.urlshortener.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;

/**
 * Configuration class for security settings in the URL shortener application.
 * <p>
 * This class configures security settings, including authentication, authorization,
 * and session management for the URL shortener application. It integrates JWT-based
 * authentication and defines the rules for HTTP security.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter authorizationFilter;

    /**
     * Configures the security filter chain to define HTTP security rules.
     * <p>
     * This method sets up rules for request authorization, allowing access to specific
     * endpoints (such as authentication and documentation endpoints) while requiring
     * authentication for all other requests. It also disables session creation and
     * enables stateless security based on JWT tokens.
     * </p>
     *
     * @param http the {@link HttpSecurity} object used to configure HTTP security
     * @return a {@link SecurityFilterChain} object to be used for request filtering
     * @throws Exception if an error occurs during the configuration of HTTP security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/api/v1/auth/**",
                                        "/api/v1/docs/**",
                                        "/api/v2/docs/**",
                                        "/api/docs/**",
                                        "/v3/api-docs/**",
                                        "/api/v2/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/error")
                                .permitAll()
                                .requestMatchers(POST, "/api/v1/url/{shortUrlCode}").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configures a password encoder used to encode user passwords.
     * <p>
     * This method creates and returns a {@link BCryptPasswordEncoder} with a strength of 4
     * for securely hashing user passwords.
     * </p>
     *
     * @return a {@link PasswordEncoder} for encoding passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    /**
     * Configures an authentication provider for authenticating users.
     * <p>
     * This method creates and returns a custom {@link AuthenticationProvider} using
     * {@link CustomUserDetailsService} for loading user details and {@link BCryptPasswordEncoder}
     * for password validation.
     * </p>
     *
     * @param userDetailsService the service used to load user details for authentication
     * @param passwordEncoder the encoder used to validate user passwords
     * @return an {@link AuthenticationProvider} used for authenticating users
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    /**
     * Configures the authentication manager for the application.
     * <p>
     * This method creates and returns an {@link AuthenticationManager} using the shared
     * {@link AuthenticationManagerBuilder} from Spring Security.
     * </p>
     *
     * @param http the {@link HttpSecurity} object to access the authentication manager builder
     * @return an {@link AuthenticationManager} for managing user authentication
     * @throws Exception if an error occurs while creating the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
