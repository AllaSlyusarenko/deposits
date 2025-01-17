package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(exclude = "idBankAccounts")
@Entity
@Table(name = "bank_accounts", schema = "account")
public class BankAccount { //2.1
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_generator")
    @SequenceGenerator(name = "bank_accounts_generator", sequenceName = "account.id_bank_accounts_sq", allocationSize = 1, initialValue = 2)
    private Integer idBankAccounts;
    @Column(name = "num_bank_accounts", columnDefinition = "numeric(20,0) unique not null")
    private BigDecimal numBankAccounts;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "currency")
    private String currency = "RUR";
    @Column(name = "is_active") //поле активный ли счет
    private Boolean isActive;


    public BankAccount() {
    }
}