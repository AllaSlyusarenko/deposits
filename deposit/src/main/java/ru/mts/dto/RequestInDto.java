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
    private DepositTermDto depositTerm; //срок вклада - выбор из списка
    private BigDecimal depositAmount;
    private TypesPercentPaymentDto typesPercentPayment; //выплата процентов
    private BankAccountInDto percentPaymentAccountId; //счет для выплаты процентов
    private BankAccountInDto depositRefundAccountId; //счет для возвращения вклада
    private BankAccountInDto depositDebitingAccountId; //счет для списания суммы депозит

    public RequestInDto() {
    }
}