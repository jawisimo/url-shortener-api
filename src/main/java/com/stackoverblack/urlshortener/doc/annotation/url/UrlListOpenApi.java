package com.stackoverblack.urlshortener.doc.annotation.url;

import com.stackoverblack.urlshortener.error.ErrorResponse;
import com.stackoverblack.urlshortener.url.dto.statistics.StatsListUrlResponse;
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
        summary = "Get URLs",
        description = "Retrieves URLs created by the authenticated user"
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "URLs provided",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = StatsListUrlResponse.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                name = "ForbiddenExample",
                                ref = "#/components/examples/ForbiddenExample")))
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UrlListOpenApi {
}
