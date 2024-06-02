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
    @SequenceGenerator(name = "bank_accounts_generator", sequenceName = "account.id_bank_accounts_sq", allocationSize = 1, initialValue = 1)
    private Integer idBankAccounts;
    @Column(name = "num_bank_accounts", columnDefinition = "numeric(20,0) unique not null")
    private BigDecimal numBankAccounts;
    @Column(name = "amount", columnDefinition = "money")
    private BigDecimal amount;

    public BankAccount() {
    }

    @Override
    public String toString() {
        return "BankAccount {" +
                "idBankAccounts=" + idBankAccounts +
                ", numBankAccounts=" + numBankAccounts +
                ", amount=" + amount +
                '}';
    }
}