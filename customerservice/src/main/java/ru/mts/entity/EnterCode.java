package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(exclude = "idEnterCode")
@Entity
@Table(name = "enter_code", schema = "customer")
public class EnterCode implements Serializable { //2.16
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enter_code_generator")
    @SequenceGenerator(name = "enter_code_generator", sequenceName = "customer.id_enter_code_sq", allocationSize = 1, initialValue = 1)
    private Integer idEnterCode;
    @Column(name = "code")
    private String code;
    @Column(name = "code_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime;

    @Column(name = "id_customer")
    private Integer idCustomer;

    public EnterCode() {
    }

    @Override
    public String toString() {
        return "EnterCode {" +
                "idEnterCode=" + idEnterCode +
                ", code='" + code + '\'' +
                ", codeDateTime=" + codeDateTime +
                ", customer=" + idCustomer +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        codeDateTime = OffsetDateTime.now();
    }
}