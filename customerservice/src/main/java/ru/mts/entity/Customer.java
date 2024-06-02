package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

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