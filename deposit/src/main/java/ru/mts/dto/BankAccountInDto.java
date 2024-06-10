package ru.mts.dto;

import lombok.Data;

@Data
public class BankAccountInDto {
    private Integer idBankAccounts;
    private String numBankAccounts;
    private Boolean isActive;

    public BankAccountInDto() {
    }
}
