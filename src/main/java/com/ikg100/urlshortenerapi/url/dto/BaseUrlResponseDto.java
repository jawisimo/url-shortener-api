package com.ikg100.urlshortenerapi.url.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"short_url_code", "long_url", "created_at", "expires_at"})
@Schema
public class BaseUrlResponseDto {
    @Schema(example = "skY8gWu")
    private String shortUrlCode;

    @Schema(example = "https://www.youtube.com")
    private String longUrl;

    private LocalDateTime createdAt;

    @Schema(example = "2028-08-30T00:00:00.000Z")
    private LocalDateTime expiresAt;
}
