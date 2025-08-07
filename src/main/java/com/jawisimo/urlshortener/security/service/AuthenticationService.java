package com.jawisimo.urlshortener.security.service;

import com.jawisimo.urlshortener.security.dto.AuthUserRequest;
import com.jawisimo.urlshortener.security.dto.AuthUserResponse;

public interface AuthenticationService  {
    AuthUserResponse authenticate(AuthUserRequest request) ;
}
