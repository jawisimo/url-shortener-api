package com.ikg100.urlshortenerapi.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"timestamp", "status", "error", "path", "message"})
@Schema
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;
    private String message;

    public static ErrorResponse createErrorResponse(String path, int status, String error, String message) {
        return new ErrorResponse(LocalDateTime.now(), status, error, path, message);
    }
}
