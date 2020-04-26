package com.gamedia.gamedia.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExchangeDTO {
    private String from;
    private List<String> to;
    private BigDecimal amount;
}
