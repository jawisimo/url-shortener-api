package com.stackoverblack.urlshortener.url.dto.operations;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class UpdateUrlRequest {

    @Schema(example = "skY8gWu")
    private String shortUrlCode;

    @Schema(example = "2028-08-30T00:00:00.000Z")
    private LocalDateTime expiresAt;

    public UpdateUrlRequest(String shortUrlCode) {
        this.shortUrlCode = shortUrlCode;
    }
}
