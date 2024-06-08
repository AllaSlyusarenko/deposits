package ru.mts.entity;

import lombok.Data;

@Data
public class DepositTerm {
    private Integer id;
    private String depositTermName;

    public DepositTerm() {
    }
}