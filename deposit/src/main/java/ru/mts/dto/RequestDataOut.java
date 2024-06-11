package ru.mts.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestDataOut {
    private Integer id; //id заявки
    private BigDecimal depositDebitingAccountId; //счет для списания суммы вклада
    private BigDecimal depositAmount; //сумма вклада
}
