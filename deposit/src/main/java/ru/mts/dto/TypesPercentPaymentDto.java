package ru.mts.dto;

import lombok.Data;

@Data
public class TypesPercentPaymentDto {
    private Integer idTypePercentPayment;
    private String typePercentPaymentPeriod;

    public TypesPercentPaymentDto() {
    }
}
