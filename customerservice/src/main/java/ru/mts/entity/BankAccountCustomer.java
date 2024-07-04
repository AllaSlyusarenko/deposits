package ru.mts.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = "idBankAccountCustomer")
@Entity
@Table(name = "bank_account_customer", schema = "customer")
public class BankAccountCustomer { //2.18
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_customer_generator")
    @SequenceGenerator(name = "bank_account_customer_generator", sequenceName = "customer.id_bank_account_customer_sq", allocationSize = 1, initialValue = 2)
    private Integer idBankAccountCustomer;
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "bank_account_id")
    private Integer bankAccountId;
}