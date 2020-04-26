package com.gamedia.gamedia.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyDTO {
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal result;
    private BigDecimal fee;
}
