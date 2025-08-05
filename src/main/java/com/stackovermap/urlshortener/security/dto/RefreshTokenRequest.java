package com.stackovermap.urlshortener.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema
public class RefreshTokenRequest {

    @Schema(example = "refreshToken")
    private String refreshToken;
}
