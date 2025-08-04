package com.ikg100.urlshortenerapi.security.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"accessToken", "refreshToken"})
@Schema
public class RefreshTokenResponse {

    @Schema(example = "accessToken")
    private String accessToken;

    @Schema(example = "refreshToken")
    private String refreshToken;

    public static RefreshTokenResponse createSuccessResponse(String accessToken, String refreshToken) {
        return new RefreshTokenResponse(accessToken, refreshToken);
    }
}
