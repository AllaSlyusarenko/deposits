package ru.mts.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "idDepositsTypes")
@Entity
@Table(name = "deposits_types", schema = "deposit")
public class DepositsType { //2.7
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposits_types_generator")
    @SequenceGenerator(name = "deposits_types_generator", sequenceName = "deposit.id_deposit_type_sq", allocationSize = 1, initialValue = 1)
    private Integer idDepositsTypes;
    @Column(name = "deposits_types_name")
    private String depositsTypesName;

    public DepositsType() {
    }

    @Override
    public String toString() {
        return depositsTypesName;
    }
}