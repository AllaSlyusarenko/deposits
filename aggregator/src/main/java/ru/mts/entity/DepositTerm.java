package ru.mts.entity;

import lombok.Data;

@Data
public class DepositTerm {
    private Integer idDepositTerm;
    private String depositTermName;

    public DepositTerm() {
    }
}