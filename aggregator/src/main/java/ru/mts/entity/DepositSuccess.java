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
                "/n Вид вклада: " + depositsType + '\'' +
                "/n Сумма: " + depositAmount +
                "/n Текущая дата: " + startDate +
                "/n Процентная ставка" + depositRate +
                '}';
    }
}