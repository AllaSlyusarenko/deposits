package ru.mts.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccount {
    private Integer idBankAccounts;
    private String numBankAccounts;
    private Boolean isActive;

    public BankAccount() {
    }
}
