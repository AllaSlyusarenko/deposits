package ru.mts.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class Request {

//    private Integer customerId; //не будет показано

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private OffsetDateTime requestDateTime;//не будет показано

    private boolean isDepositRefill; //пополнение депозита - true/false
    private boolean isReductionOfDeposit; //уменьшение депозита - true/false
    private DepositTerm depositTerm; //срок вклада - выбор из списка
    private BigDecimal depositAmount;
    private TypesPercentPayment typesPercentPayment; //выплата процентов

    private BankAccount depositDebitingAccountId; //счет для списания суммы депозит
    private BankAccount percentPaymentAccountId; //счет для выплаты процентов
    private BankAccount depositRefundAccountId; //счет для возвращения вклада


    public Request() {
    }

//    @PrePersist
//    protected void onRequestDateTime() {
//        requestDateTime = OffsetDateTime.now();
//    }
}