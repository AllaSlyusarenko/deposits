package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"idDeposit", "depositsType", "depositTerm", "depositRate", "typesPercentPayment"})
@Entity
@Table(name = "deposits", schema = "deposit")
public class Deposit { //2.6
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_generator")
    @SequenceGenerator(name = "requests_generator", sequenceName = "deposit.id_deposit_sq", allocationSize = 1, initialValue = 1)
    private Integer idDeposit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_request")
    private Request request;
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "is_deposit_refill")
    private boolean isDepositRefill; //пополнение депозита
    @Column(name = "is_reduction_of_deposit")
    private boolean isReductionOfDeposit; //уменьшение депозита

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deposits_types")
    private DepositsType depositsType;  // вид вклада
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deposit_term")
    private DepositTerm depositTerm; //срок вклада
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime endDate;
    @Column(name = "deposit_amount", columnDefinition = "money")
    private BigDecimal depositAmount; //сумма вклада

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deposit_rate")
    private DepositRate depositRate; //процент ставка зависит от: вид депозита, срок, сумма

//    @Column(name = "code")
//    private String code; //изначально пусто
//    @Column(name = "code_date_time")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private OffsetDateTime codeDateTime; //изначально пусто

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_percent_payment")
    private TypesPercentPayment typesPercentPayment; //выплата процентов - период

    @Column(name = "deposit_account_id", columnDefinition = "numeric(20,0) not null")
    private BigDecimal depositAccountId; //счет депозита
    @Column(name = "deposit_debiting_account_id", columnDefinition = "numeric(20,0) not null")
    private BigDecimal depositDebitingAccountId; //счет для списания суммы депозит

    @Column(name = "percent_payment_account_id", columnDefinition = "numeric(20,0) not null")
    private BigDecimal percentPaymentAccountId; //счет для выплаты процентов

    @Column(name = "deposit_refund_account_id", columnDefinition = "numeric(20,0) not null")
    private BigDecimal depositRefundAccountId; //счет для возвращения вклада

    @Column(name = "is_active")
    private boolean isActive; //действующий ли депозит

    public Deposit() {
    }

    @Override
    public String toString() {
        return "Deposit {" +
                "idDeposit=" + idDeposit +
                ", request=" + request.getIdRequest() +
                ", customerId=" + customerId +
                ", isDepositRefill=" + isDepositRefill +
                ", isReductionOfDeposit=" + isReductionOfDeposit +
                ", depositsType=" + depositsType.getDepositsTypesName() +
                ", startDate=" + startDate +
                ", depositTerm=" + depositTerm.getDepositTermName() +
                ", endDate=" + endDate +
                ", depositAmount=" + depositAmount +
                ", depositRate=" + depositRate.getDepositRate() +
                ", typesPercentPayment=" + typesPercentPayment.getTypePercentPaymentPeriod() +
                ", depositAccountId=" + depositAccountId +
                ", depositDebitingAccountId=" + depositDebitingAccountId +
                ", percentPaymentAccountId=" + percentPaymentAccountId +
                ", depositRefundAccountId=" + depositRefundAccountId +
                ", isActive=" + isActive +
                '}';
    }
}