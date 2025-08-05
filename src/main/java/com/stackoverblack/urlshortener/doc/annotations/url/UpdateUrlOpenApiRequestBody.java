package com.stackoverblack.urlshortener.doc.annotations.url;

import com.stackoverblack.urlshortener.url.dto.operations.UpdateUrlRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(schema = @Schema(implementation = UpdateUrlRequest.class)))
public @interface UpdateUrlOpenApiRequestBody {
}
