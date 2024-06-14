package ru.mts.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CloseDepositDto {
    private BigDecimal depositAccountId; //счет депозита
    private BigDecimal depositRefundAccountId; //счет для возвращения вклада
    private BigDecimal depositAmount; //сумма вклада

    public CloseDepositDto() {
    }
}