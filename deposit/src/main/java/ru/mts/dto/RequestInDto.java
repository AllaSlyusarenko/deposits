package ru.mts.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class RequestInDto {

    //    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private OffsetDateTime requestDateTime;
    private boolean isDepositRefill; //пополнение депозита - true/false
    private boolean isReductionOfDeposit; //уменьшение депозита - true/false
    private String depositTerm; //срок вклада - выбор из списка
    private BigDecimal depositAmount;
    private String typesPercentPayment; //выплата процентов
    private String percentPaymentAccountId; //счет для выплаты процентов
    private String depositRefundAccountId; //счет для возвращения вклада
    private String depositDebitingAccountId; //счет для списания суммы депозит

    public RequestInDto() {
    }
}