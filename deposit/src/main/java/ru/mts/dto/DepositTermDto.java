package ru.mts.dto;

import lombok.Data;

@Data
public class DepositTermDto {
    private Integer idDepositTerm;
    private String depositTermName;

    public DepositTermDto() {
    }
}
