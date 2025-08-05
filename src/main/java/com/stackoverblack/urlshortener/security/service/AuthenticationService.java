package com.stackoverblack.urlshortener.security.service;

import com.stackoverblack.urlshortener.security.dto.AuthUserRequest;
import com.stackoverblack.urlshortener.security.dto.AuthUserResponse;

public interface AuthenticationService  {
    AuthUserResponse authenticate(AuthUserRequest request) ;
}
