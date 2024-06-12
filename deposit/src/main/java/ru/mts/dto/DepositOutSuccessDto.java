package ru.mts.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class DepositOutSuccessDto {
    private Integer idRequest;
    private String depositsType;
    private BigDecimal depositAmount;
    private OffsetDateTime startDate;
    private BigDecimal depositRate;

    public DepositOutSuccessDto() {
    }
}