package ru.mts.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TypesPercentPayment implements Serializable {
    private Integer idTypePercentPayment;
    private String typePercentPaymentPeriod;

    public TypesPercentPayment() {
    }
}