package com.stackoverblack.urlshortener.security.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"accessToken", "refreshToken"})
@Schema
public class AuthUserResponse {

    @Schema(example = "accessToken")
    private String accessToken;

    @Schema(example = "refreshToken")
    private String refreshToken;

    public static AuthUserResponse createSuccessResponse(String accessToken, String refreshToken) {
        return new AuthUserResponse(accessToken, refreshToken);
    }
}
