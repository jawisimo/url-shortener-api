package com.stackoverblack.urlshortener.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class RegisterUserRequest {

    @Schema(example = "user123")
    private String login;

    @Schema(example = "test@email.com")
    private String email;

    @Schema(example = "passWord123")
    private String password;
}

