package com.stackoverblack.urlshortener.url.dto.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class UrlResponse {

    @JsonProperty("url")
    private UrlDto urlDto;

    public static UrlResponse createSuccessResponse(UrlDto urlDto) {
        return new UrlResponse(urlDto);
    }
}
