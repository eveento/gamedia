package com.gamedia.gamedia.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ResponseExchangeDTO {
    private String from;
    private Map<String,CurrencyDTO> currencyDTO;
}
