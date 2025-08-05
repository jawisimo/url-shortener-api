package com.stackovermap.urlshortener.doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * Configuration class for setting up OpenAPI documentation using Springdoc OpenAPI and Swagger.
 * Defines security scheme, error response examples, and provides custom OpenAPI customization.
 */
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class OpenApiConfig {

    /**
     * Creates a reusable error example with the given status, error, and message.
     *
     * @param status  HTTP status code.
     * @param error   short error description.
     * @param message detailed error message.
     * @return Example instance representing the error.
     */
    private static Example createErrorExample(int status, String error, String message) {
        LinkedHashMap<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("status", status);
        errorBody.put("error", error);
        errorBody.put("path", "/api/v1/**");
        errorBody.put("message", message);
        return new Example().value(errorBody);
    }

    /** Example for 400 Bad Request error. */
    private static final Example BAD_REQUEST_EXAMPLE = createErrorExample(
            400,
            "Bad Request",
            "Bad request appropriate message");

    /** Example for 401 Unauthorized error. */
    private static final Example UNAUTHORIZED_EXAMPLE = createErrorExample(
            401,
            "Unauthorized",
            "Unauthorized appropriate message");

    /** Example for 403 Forbidden error. */
    private static final Example FORBIDDEN_EXAMPLE = createErrorExample(
            403,
            "Forbidden",
            "Forbidden appropriate message");

    /** Example for 404 Not Found error. */
    private static final Example NOT_FOUND_EXAMPLE = createErrorExample(
            404,
            "Not found",
            "Resource not found appropriate message");

    /** Example for 409 Conflict error. */
    private static final Example CONFLICT_EXAMPLE = createErrorExample(
            409,
            "Conflict",
            "Conflict appropriate message");

    /** Example for 500 Internal Server Error. */
    private static final Example INTERNAL_SERVER_ERROR_EXAMPLE = createErrorExample(
            500,
            "Internal Server Error",
            "Internal server error appropriate message");

    /**
     * Bean that customizes OpenAPI documentation with general API info (title, version).
     *
     * @return the customized OpenAPI configuration.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API documentation. URL SHORTENER Rest API.")
                        .version("1.0"));
    }

    /**
     * Customizes OpenAPI by adding predefined error response examples to the OpenAPI components.
     *
     * @return the OpenApiCustomizer to apply examples.
     */
    @Bean
    public OpenApiCustomizer customizerOpenApi() {
        return openApi -> openApi.getComponents()
                .addExamples("BadRequestExample", BAD_REQUEST_EXAMPLE)
                .addExamples("UnauthorizedExample", UNAUTHORIZED_EXAMPLE)
                .addExamples("ForbiddenExample", FORBIDDEN_EXAMPLE)
                .addExamples("NotFoundExample", NOT_FOUND_EXAMPLE)
                .addExamples("ConflictExample", CONFLICT_EXAMPLE)
                .addExamples("InternalServerErrorExample", INTERNAL_SERVER_ERROR_EXAMPLE);
    }
}
