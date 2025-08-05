package com.stackoverblack.urlshortener.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@Schema
public class AuthUserRequest {

    @Schema(example = "test@email.com")
    private String identifier;

    @Schema(example = "passWord123")
    private String password;
}
