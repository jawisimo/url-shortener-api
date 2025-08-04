package com.ikg100.urlshortenerapi.openapi.annotations.url;

import com.ikg100.urlshortenerapi.error.ErrorResponse;
import com.ikg100.urlshortenerapi.url.dto.operations.UrlResponse;
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

@Operation(summary = "Update a shortened URL", description = "Modifies the stored URL mapping")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Long URL returned",
                content = @Content(schema = @Schema(implementation = UrlResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Data is not correct (e.g., incorrect short URL code or expires date)",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "BadRequestExample",
                                ref = "#/components/examples/BadRequestExample"))),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "ForbiddenExample",
                                ref = "#/components/examples/ForbiddenExample"))),
        @ApiResponse(
                responseCode = "404",
                description = "URL not found",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "NotFoundExample",
                                ref = "#/components/examples/NotFoundExample")))
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateUrlOpenApi {
}
