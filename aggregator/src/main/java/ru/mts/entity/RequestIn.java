package ru.mts.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RequestIn implements Serializable {
    private String isDepositRefill; //пополнение депозита - true/false
    private String isReductionOfDeposit; //уменьшение депозита - true/false
    private DepositTerm depositTerm; //срок вклада - выбор из списка
    private BigDecimal depositAmount;
    private TypesPercentPayment typesPercentPayment; //выплата процентов

    private BankAccount depositDebitingAccountId; //счет для списания суммы депозит
    private BankAccount percentPaymentAccountId; //счет для выплаты процентов
    private BankAccount depositRefundAccountId; //счет для возвращения вклада

    public RequestIn() {
    }
}
