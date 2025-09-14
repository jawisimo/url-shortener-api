package com.jawisimo.urlshortener.doc.annotation.security;

import com.jawisimo.urlshortener.security.dto.AuthUserRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(schema = @Schema(implementation = AuthUserRequest.class)))
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AuthenticateUserRequestBodyOpenApi {
}
