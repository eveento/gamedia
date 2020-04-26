package com.gamedia.gamedia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoinBeneDTO {
    @JsonProperty("Response")
    private String response;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("HasWarning")
    private String warning;
    @JsonProperty("Type")
    private Integer type;
    @JsonProperty("RateLimit")
    private Object rateLimit;
    @JsonProperty("Data")
    private List<DataCoinBeneDTO> data;
}
