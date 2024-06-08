package ru.mts.entity;

import lombok.Data;

@Data
public class TypesPercentPayment {
    private Integer idTypePercentPayment;
    private String typePercentPaymentPeriod;

    public TypesPercentPayment() {
    }
}