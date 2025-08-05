package com.stackoverblack.urlshortener.security;

import com.stackoverblack.urlshortener.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of {@link UserDetails} to integrate the application's {@link User} model
 * with Spring Security.
 * <p>
 * Uses the {@link User} entity to provide authentication and authorization details.
 *
 * @param user the domain user entity used for security context
 */
public record CustomUserDetails(User user) implements UserDetails {

    /**
     * Returns the authorities granted to the user.
     * <p>
     * In this implementation, it's just a single role from the user.
     *
     * @return a list with a single {@link SimpleGrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the user's hashed password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return the user's login
     */
    @Override
    public String getUsername() {
        return user.getLogin();
    }

    /**
     * Indicates whether the user's account has expired.
     * <p>
     * Default behavior is taken from {@link UserDetails#isAccountNonExpired()}.
     *
     * @return true if the account is non-expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return true if credentials are non-expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
