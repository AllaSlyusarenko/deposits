package ru.mts.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class DepositShort {
    private Integer idDeposit;
    private String depositsType;
    private String depositAmount;
    private OffsetDateTime endDate;
    private BigDecimal depositRate;

    public DepositShort() {
    }

    @Override
    public String toString() {
        return "Вклад: " +
                "depositsType='" + depositsType + '\'' +
                ", depositAmount=" + depositAmount +
                ", endDate=" + endDate +
                ", depositRate=" + depositRate +
                '}';
    }
}