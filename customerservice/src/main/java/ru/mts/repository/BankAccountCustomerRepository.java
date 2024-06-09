package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.BankAccountCustomer;

import java.util.List;

public interface BankAccountCustomerRepository extends JpaRepository<BankAccountCustomer, Integer> {
    List<BankAccountCustomer> findAllByCustomerId(Integer customerId);
}