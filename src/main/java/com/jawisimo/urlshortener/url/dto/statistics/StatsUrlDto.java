package com.jawisimo.urlshortener.url.dto.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jawisimo.urlshortener.url.dto.BaseUrlResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"visits", "isActive"})
public class StatsUrlDto extends BaseUrlResponseDto {

    @Schema(example = "1048")
    private long visits;

    @Schema(example = "true")
    private boolean isActive;
}
