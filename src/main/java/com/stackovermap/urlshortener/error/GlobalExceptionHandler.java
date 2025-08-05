package com.stackovermap.urlshortener.error;

import com.stackovermap.urlshortener.error.exception.ResourceNotFoundException;
import com.stackovermap.urlshortener.error.exception.UnauthorizedException;
import com.stackovermap.urlshortener.error.exception.UserExistsException;
import com.stackovermap.urlshortener.error.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * Global exception handler for handling various types of exceptions globally in the application.
 * This class handles exceptions like validation errors, unauthorized access, bad credentials,
 * resource not found, etc., and returns structured error responses with appropriate HTTP status codes.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles ValidationException and returns a BAD_REQUEST response with the error details.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleUserValidateException(ValidationException e,
                                                                     HttpServletRequest request) {
        log.warn("ValidationException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles UserExistsException and returns a CONFLICT response with the error details.
     */
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExistsException(UserExistsException e,
                                                                   HttpServletRequest request) {
        log.warn("UserExistsException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles BadCredentialsException and returns an UNAUTHORIZED response with the error details.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e,
                                                                       HttpServletRequest request) {
        log.warn("BadCredentialsException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles UnauthorizedException and returns an UNAUTHORIZED response with the error details.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e,
                                                                     HttpServletRequest request) {
        log.warn("UnauthorizedException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles UsernameNotFoundException and returns an UNAUTHORIZED response with the error details.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e,
                                                                         HttpServletRequest request) {
        log.warn("UsernameNotFoundException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles ResourceNotFoundException and returns a NOT_FOUND response with the error details.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                         HttpServletRequest request) {
        log.warn("UrlException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles IOException and returns an INTERNAL_SERVER_ERROR response with the error details.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e, HttpServletRequest request) {
        log.error("IOException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Handles JsonParseException and returns a BAD_REQUEST response with the error details.
     */
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(JsonParseException e, HttpServletRequest request) {
        log.error("JsonParseException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles HttpMessageNotReadableException and returns a BAD_REQUEST response with the error details.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                               HttpServletRequest request) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles HttpMediaTypeNotAcceptableException and returns a BAD_REQUEST response with the error details.
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e,
                                                                                   HttpServletRequest request) {
        log.error("HttpMediaTypeNotAcceptableException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles all other exceptions not caught by specific handlers and returns an INTERNAL_SERVER_ERROR response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception occurred: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.createErrorResponse(
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
