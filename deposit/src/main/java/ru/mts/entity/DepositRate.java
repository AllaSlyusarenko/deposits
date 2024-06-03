package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(exclude = "idDepositRate")
@Entity
@Table(name = "deposit_rate", schema = "deposit")
public class DepositRate { //2.10
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_rate_generator")
    @SequenceGenerator(name = "deposit_rate_generator", sequenceName = "deposit.id_deposit_rate_sq", allocationSize = 1, initialValue = 1)
    private Integer idDepositRate;
    @Column(name = "deposit_rate", columnDefinition = "decimal(4,2) not null")
    private BigDecimal depositRate;

    public DepositRate() {
    }

    @Override
    public String toString() {
        return "DepositRate {" +
                "idDepositRate=" + idDepositRate +
                ", depositRate=" + depositRate +
                '}';
    }
}