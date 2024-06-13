package ru.mts.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class DepositOutFullDto {
    private Integer idDeposit;
    private BigDecimal depositAccountId;
    private String depositTerm;
    private String depositsType;
    private BigDecimal depositAmount;
    private BigDecimal depositRate;
    private String typesPercentPayment;
    private BigDecimal percentPaymentAccountId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private BigDecimal depositRefundAccountId;

    public DepositOutFullDto() {
    }
}