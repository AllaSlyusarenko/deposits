package ru.mts.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepositTerm  implements Serializable {
    private Integer idDepositTerm;
    private String depositTermName;

    public DepositTerm() {
    }
}