package ru.mts.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "idTypePercentPayment")
@Entity
@Table(name = "types_percent_payment", schema = "deposit")
public class TypesPercentPayment { //2.8
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "types_percent_payment_generator")
    @SequenceGenerator(name = "types_percent_payment_generator", sequenceName = "deposit.id_type_percent_payment_sq", allocationSize = 1, initialValue = 1)
    private Integer idTypePercentPayment;
    @Column(name = "type_percent_payment_period")
    private String typePercentPaymentPeriod;

    public TypesPercentPayment() {
    }

    @Override
    public String toString() {
        return typePercentPaymentPeriod;
    }
}