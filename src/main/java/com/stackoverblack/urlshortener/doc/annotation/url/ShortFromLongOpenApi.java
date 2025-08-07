package com.stackoverblack.urlshortener.doc.annotation.url;

import com.stackoverblack.urlshortener.error.ErrorResponse;
import com.stackoverblack.urlshortener.url.dto.operations.UrlResponse;
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
        summary = "Shorten a long URL",
        description = "Creates a short version of the provided long URL")
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Short URL code returned",
                content = @Content(schema = @Schema(implementation = UrlResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Data is not correct (e.g., incorrect long url or expires date)",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "BadRequestExample",
                                ref = "#/components/examples/BadRequestExample")
                )),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "ForbiddenExample",
                                ref = "#/components/examples/ForbiddenExample"))),
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ShortFromLongOpenApi {
}
