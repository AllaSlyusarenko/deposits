package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(exclude = "idCustomers")
@Entity
@Table(name = "customers", schema = "customer")
public class Customer implements Serializable { //2.2
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_generator")
    @SequenceGenerator(name = "customer_generator", sequenceName = "customer.id_customers_sq", allocationSize = 1, initialValue = 4)
    private Integer idCustomers;
    @Column(name = "phone_number")
    private String phoneNumber;

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer {" +
                "idCustomers=" + idCustomers +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}