package ru.mts.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RequestData implements Serializable {
    private Integer id; //id заявки
    private BigDecimal depositDebitingAccountId; //счет для списания суммы вклада
    private BigDecimal depositAmount; //сумма вклада
}
