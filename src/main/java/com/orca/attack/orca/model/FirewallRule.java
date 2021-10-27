package com.orca.attack.orca.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirewallRule {
    @JsonProperty("fw_id")
    private String fwId;
    @JsonProperty("source_tag")
    private String sourceTag;
    @JsonProperty("dest_tag")
    private String destTag;
}
