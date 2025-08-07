package com.jawisimo.urlshortener.url.dto.statistics;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"totalVisits", "urls"})
public class StatsListUrlResponse {

    @Schema(example = "1048")
    private Long totalVisits;

    private List<StatsUrlDto> urls;

    public static StatsListUrlResponse createSuccessResponse(Long totalVisits, List<StatsUrlDto> urls) {
        return new StatsListUrlResponse(totalVisits, urls);
    }
}
