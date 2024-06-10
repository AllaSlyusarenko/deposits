package ru.mts.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountOutDto {
    private Integer idBankAccounts;
    private String numBankAccounts;
    private Boolean isActive;

    public BankAccountOutDto() {
    }
}
