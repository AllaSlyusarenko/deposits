package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = "idDepositTerm")
@Entity
@Table(name = "deposit_term", schema = "deposit")
public class DepositTerm { //2.13
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_term_generator")
    @SequenceGenerator(name = "deposit_term_generator", sequenceName = "deposit.id_deposit_term_sq", allocationSize = 1, initialValue = 1)
    private Integer idDepositTerm;
    @Column(name = "deposit_term_name")
    private String depositTermName;

    public DepositTerm() {
    }

    @Override
    public String toString() {
        return "DepositTerm {" +
                "idDepositTerm=" + idDepositTerm +
                ", depositTermName='" + depositTermName + '\'' +
                '}';
    }
}