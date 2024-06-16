package ru.mts.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class DepositFull {
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

    public DepositFull() {
    }

    @Override
    public String toString() {
        return "Вклад: Код продукта: " + idDeposit +
                ",  Счет вклада: " + depositAccountId +
                ",  Срок: " + depositTerm +
                ",  Вид вклада: " + depositsType +
                ",  Сумма: " + depositAmount + " RUR" +
                ",  Ставка: " + depositRate + " %" +
                ",  Период выплат: " + typesPercentPayment +
                ",  Счет для выплаты процентов: " + percentPaymentAccountId +
                ",  Дата открытия: " + startDate.toLocalDateTime().toLocalDate() +
                ",  Конечная дата: " + endDate.toLocalDateTime().toLocalDate() +
                ",  Счет возвращения вклада: " + depositRefundAccountId;
    }
}