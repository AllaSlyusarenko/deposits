package ru.mts.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "idRequest")
@Entity
@Table(name = "requests", schema = "deposit")
public class Request { //2.3
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_generator")
    @SequenceGenerator(name = "requests_generator", sequenceName = "deposit.id_request_sq", allocationSize = 1, initialValue = 1)
    private Integer idRequest;
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "request_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime requestDateTime;
//    @Column(name = "code")
//    private String code; //изначально пусто
//    @Column(name = "code_date_time")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private OffsetDateTime codeDateTime; //изначально пусто
    @Column(name = "is_deposit_refill")
    private boolean isDepositRefill; //пополнение депозита
    @Column(name = "is_reduction_of_deposit")
    private boolean isReductionOfDeposit; //уменьшение депозита

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deposit_term")
    private DepositTerm depositTerm; //срок вклада
    @Column(name = "deposit_amount", columnDefinition = "money")
    private BigDecimal depositAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_percent_payment")
    private TypesPercentPayment typesPercentPayment; //выплата процентов

    @Column(name = "percent_payment_account_id", columnDefinition = "numeric(20,0) not null")
    private BigDecimal percentPaymentAccountId; //счет для выплаты процентов

    @Column(name = "deposit_refund_account_id", columnDefinition = "numeric(20,0) not null")
    private BigDecimal depositRefundAccountId; //счет для возвращения вклада

    @Column(name = "deposit_debiting_account_id", columnDefinition = "numeric(20,0) not null")
    private BigDecimal depositDebitingAccountId; //счет для списания суммы депозит
//    @OneToMany
//    @JoinColumn(name = "id")
//    private CurrentRequestStatus currentRequestStatus;


    public Request() {
    }

//    @PrePersist
//    protected void onRequestDateTime() {
//        requestDateTime = OffsetDateTime.now();
//    }

    @Override
    public String toString() {
        return "Request{" +
                "idRequest=" + idRequest +
                ", customerId=" + customerId +
                ", requestDateTime=" + requestDateTime +
                ", isDepositRefill=" + isDepositRefill +
                ", isReductionOfDeposit=" + isReductionOfDeposit +
                ", depositTerm=" + depositTerm +
                ", depositAmount=" + depositAmount +
                ", typesPercentPayment=" + typesPercentPayment.getTypePercentPaymentPeriod() +
                ", percentPaymentAccountId=" + percentPaymentAccountId +
                ", depositRefundAccountId=" + depositRefundAccountId +
                ", depositDebitingAccountId=" + depositDebitingAccountId +
                '}';
    }
}