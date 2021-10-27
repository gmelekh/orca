package com.orca.attack.orca.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponse {
    @JsonProperty("vm_count")
    private Integer vmCount;
    @JsonProperty("request_count")
    private Integer requestCount;
    @JsonProperty("average_request_time")
    private String averageRequestTime;
}
