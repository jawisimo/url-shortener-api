package com.stackovermap.urlshortener.url.dto.operations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class GetShortUrlRequest {

    @Schema(example = "https://www.youtube.com/")
    private String longUrl;

    @Schema(example = "2028-08-30T00:00:00.000Z")
    private LocalDateTime expiresAt;

    public GetShortUrlRequest(String longUrl) {
        this.longUrl = longUrl;
    }
}
