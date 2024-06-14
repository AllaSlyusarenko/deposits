package ru.mts.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CloseDeposit {
    private BigDecimal depositAccountId; //счет депозита
    private BigDecimal depositRefundAccountId; //счет для возвращения вклада
    private BigDecimal depositAmount; //сумма вклада

    public CloseDeposit() {
    }
}
