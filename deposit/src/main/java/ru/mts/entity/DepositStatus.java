package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "idDepositStatus")
@Entity
@Table(name = "deposit_statuses", schema = "deposit")
public class DepositStatus { //2.12
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_statuses_generator")
    @SequenceGenerator(name = "deposit_statuses_generator", sequenceName = "deposit.id_deposit_status_sq", allocationSize = 1, initialValue = 1)
    private Integer idDepositStatus;
    @Column(name = "deposit_status_name")
    private String depositStatusName;

    public DepositStatus() {
    }

    @Override
    public String toString() {
        return depositStatusName;
    }
}