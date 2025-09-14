package com.jawisimo.urlshortener.doc.annotation.security;

import com.jawisimo.urlshortener.error.ErrorResponse;
import com.jawisimo.urlshortener.security.dto.AuthUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
        summary = "User log in",
        description = "Authenticates a user by login or email and returns the JWT access token and refresh token")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "User successfully authenticated",
                content = @Content(schema = @Schema(implementation = AuthUserResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Input data is not correct (e.g., incorrect login, email or password format)",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "BadRequestExample",
                                ref = "#/components/examples/BadRequestExample"))),
        @ApiResponse(
                responseCode = "401",
                description = "User not authenticated (e.g., wrong password)",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "UnauthorizedExample",
                                ref = "#/components/examples/UnauthorizedExample")))
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthenticateUserOpenApi {
}
