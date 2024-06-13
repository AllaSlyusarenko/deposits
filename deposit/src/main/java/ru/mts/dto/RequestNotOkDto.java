package ru.mts.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class RequestNotOkDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime createdDateTime;
    private Integer id; //id заявки
    private BigDecimal depositDebitingAccountId; //счет для списания суммы вклада
    private BigDecimal depositAmount; //сумма вклада

    public RequestNotOkDto() {
    }
}
