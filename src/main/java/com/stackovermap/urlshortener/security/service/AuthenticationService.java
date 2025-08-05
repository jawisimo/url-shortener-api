package com.stackovermap.urlshortener.security.service;

import com.stackovermap.urlshortener.security.dto.AuthUserRequest;
import com.stackovermap.urlshortener.security.dto.AuthUserResponse;

public interface AuthenticationService  {
    AuthUserResponse authenticate(AuthUserRequest request) ;
}
