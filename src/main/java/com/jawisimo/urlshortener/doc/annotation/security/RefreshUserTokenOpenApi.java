package com.jawisimo.urlshortener.doc.annotation.security;

import com.jawisimo.urlshortener.error.ErrorResponse;
import com.jawisimo.urlshortener.security.dto.RefreshTokenResponse;
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
        summary = "User refresh token",
        description = "Refresh user JWT token")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "JWT token was refreshed successfully",
                content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class))),
        @ApiResponse(
                responseCode = "401",
                description = "JWT token is not valid",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "UnauthorizedExample",
                                ref = "#/components/examples/UnauthorizedExample")))
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RefreshUserTokenOpenApi {
}
