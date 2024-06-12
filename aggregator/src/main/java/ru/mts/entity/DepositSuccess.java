package ru.mts.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class DepositSuccess {
    private Integer idRequest;
    private String depositsType;
    private BigDecimal depositAmount;
    private OffsetDateTime startDate;
    private BigDecimal depositRate;

    public DepositSuccess() {
    }

    @Override
    public String toString() {
        return "Код заявки: " + idRequest +
                ";  Вид вклада: " + depositsType +
                ";  Сумма: " + depositAmount + " RUR" +
                ";  Текущая дата: " + startDate.toLocalDateTime().toLocalDate() +
                ";  Процентная ставка: " + depositRate + "%";
    }
}