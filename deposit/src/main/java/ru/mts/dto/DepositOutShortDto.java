package ru.mts.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class DepositOutShortDto {
    private Integer idDeposit;
    private String depositsType;
    private String depositAmount;
    private OffsetDateTime endDate;
    private BigDecimal depositRate;

    public DepositOutShortDto() {
    }
}