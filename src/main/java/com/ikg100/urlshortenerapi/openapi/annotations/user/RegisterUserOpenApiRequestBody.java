package com.ikg100.urlshortenerapi.openapi.annotations.user;

import com.ikg100.urlshortenerapi.user.dto.RegisterUserRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(schema = @Schema(implementation = RegisterUserRequest.class)))
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RegisterUserOpenApiRequestBody {
}
