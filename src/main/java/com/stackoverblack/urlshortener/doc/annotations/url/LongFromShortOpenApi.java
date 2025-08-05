package com.stackoverblack.urlshortener.doc.annotations.url;

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
        summary = "Retrieve a long URL",
        description = "Finds the original URL from a given short URL code")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Long URL returned",
                content = @Content(schema = @Schema(implementation = UrlResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Short url code is not correct.",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "BadRequestExample",
                                ref = "#/components/examples/BadRequestExample")))
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LongFromShortOpenApi {
}
