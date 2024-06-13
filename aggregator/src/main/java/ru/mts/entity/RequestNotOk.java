package ru.mts.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class RequestNotOk {
    private OffsetDateTime createdDateTime;
    private Integer id; //id заявки
    private BigDecimal depositDebitingAccountId; //счет для списания суммы вклада
    private BigDecimal depositAmount; //сумма вклада

    public RequestNotOk() {
    }
}
