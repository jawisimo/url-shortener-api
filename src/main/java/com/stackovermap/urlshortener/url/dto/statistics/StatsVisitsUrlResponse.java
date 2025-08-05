package com.stackovermap.urlshortener.url.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class StatsVisitsUrlResponse {

    @Schema(example = "1545")
    private Long visits;

    public static StatsVisitsUrlResponse createSuccessResponse(Long visits) {
        return new StatsVisitsUrlResponse(visits);
    }
}
