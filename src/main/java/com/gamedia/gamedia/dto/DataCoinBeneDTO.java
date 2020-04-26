package com.gamedia.gamedia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataCoinBeneDTO {
    @JsonProperty("toSymbol")
    private String toSymbol;
    @JsonProperty("fromSymbol")
    private String fromSymbol;
    @JsonProperty("volume")
    private String volume;
}
