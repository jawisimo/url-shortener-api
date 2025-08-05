package com.stackoverblack.urlshortener.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"login", "email"})
@Schema
public class RegisterUserResponse {

    @Schema(example = "user123")
    private String login;

    @Schema(example = "test@mail.com")
    private String email;

    public static RegisterUserResponse createSuccessResponse(String login, String email) {
        return new RegisterUserResponse(login, email);
    }
}

