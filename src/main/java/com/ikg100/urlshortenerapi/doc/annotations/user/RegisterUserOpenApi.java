package com.ikg100.urlshortenerapi.doc.annotations.user;

import com.ikg100.urlshortenerapi.error.ErrorResponse;
import com.ikg100.urlshortenerapi.user.dto.RegisterUserResponse;
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
        summary = "Register a new user",
        description = "Registers a new user with provided credentials")
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "User registered successfully",
                content = @Content(schema = @Schema(implementation = RegisterUserResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Input data is not correct (e.g., incorrect login or email format)",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "BadRequestExample",
                                ref = "#/components/examples/BadRequestExample"))),
        @ApiResponse(
                responseCode = "409",
                description = "User with login or email already exists",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "ConflictExample",
                                ref = "#/components/examples/ConflictExample")))
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterUserOpenApi {
}
