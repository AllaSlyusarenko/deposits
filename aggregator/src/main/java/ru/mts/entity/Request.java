package ru.mts.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Request implements Serializable {

//    private Integer customerId; //не будет показано

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private OffsetDateTime requestDateTime;//не будет показано

    private boolean isDepositRefill; //пополнение депозита - true/false
    private boolean isReductionOfDeposit; //уменьшение депозита - true/false
    private String depositTerm; //срок вклада - выбор из списка
    private BigDecimal depositAmount;
    private String typesPercentPayment; //выплата процентов

    private String depositDebitingAccountId; //счет для списания суммы депозит
    private String percentPaymentAccountId; //счет для выплаты процентов
    private String depositRefundAccountId; //счет для возвращения вклада


    public Request() {
    }

//    @PrePersist
//    protected void onRequestDateTime() {
//        requestDateTime = OffsetDateTime.now();
//    }
}