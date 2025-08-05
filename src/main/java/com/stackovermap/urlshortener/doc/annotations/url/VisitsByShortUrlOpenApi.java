package com.stackovermap.urlshortener.doc.annotations.url;

import com.stackovermap.urlshortener.error.ErrorResponse;
import com.stackovermap.urlshortener.url.dto.statistics.StatsVisitsUrlResponse;
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
        summary = "Get visit count for a URL",
        description = "Retrieves the number of times a short URL has been visited")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "URL provided",
                content = @Content(schema = @Schema(implementation = StatsVisitsUrlResponse.class))),
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
public @interface VisitsByShortUrlOpenApi {
}
