package ru.mts.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BankAccountOutDto implements Serializable {
    private Integer idBankAccounts;
    private String numBankAccounts;
    private Boolean isActive;

    public BankAccountOutDto() {
    }
}
