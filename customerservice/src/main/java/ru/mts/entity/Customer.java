package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(exclude = "idCustomers")
@Entity
@Table(name = "customers", schema = "customer")
public class Customer { //2.2
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_generator")
    @SequenceGenerator(name = "customer_generator", sequenceName = "customer.id_customers_sq", allocationSize = 1, initialValue = 1)
    private Integer idCustomers;
    @Column(name = "bank_account_id")
    private BigDecimal bankAccountId;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "code")
    private String code; //изначально пусто
    @Column(name = "code_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime; //изначально пусто

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer {" +
                "idCustomers=" + idCustomers +
                ", bankAccountId=" + bankAccountId +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}