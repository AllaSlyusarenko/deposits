package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(exclude = "idRequestCode")
@Entity
@Table(name = "deposit_code", schema = "deposit")
public class DepositCode { //2.18
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_code_generator")
    @SequenceGenerator(name = "deposit_code_generator", sequenceName = "deposit.id_deposit_code_sq", allocationSize = 1, initialValue = 1)
    private Integer idDepositCode;
    @Column(name = "code")
    private String code;
    @Column(name = "code_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime;
    @Column(name = "id_deposit")
    private Integer idDeposit;

    public DepositCode() {
    }
    @PrePersist
    protected void onDepositDateTime() {
        codeDateTime = OffsetDateTime.now();
    }

    @Override
    public String toString() {
        return "DepositCode {" +
                "idDepositCode=" + idDepositCode +
                ", code='" + code + '\'' +
                ", codeDateTime=" + codeDateTime +
                ", idDeposit=" + idDeposit +
                '}';
    }
}
