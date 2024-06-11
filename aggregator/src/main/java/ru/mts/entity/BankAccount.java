package ru.mts.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BankAccount implements Serializable {
    private Integer idBankAccounts;
    private String numBankAccounts;
    private Boolean isActive;

    public BankAccount() {
    }
}
