package com.ikg100.urlshortenerapi.security.service;

import com.ikg100.urlshortenerapi.security.dto.AuthUserRequest;
import com.ikg100.urlshortenerapi.security.dto.AuthUserResponse;

public interface AuthenticationService  {
    AuthUserResponse authenticate(AuthUserRequest request) ;
}
