package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByIdCustomers(Integer id);

    Optional<Customer> findByPhoneNumber(String phoneNumber);
//    Optional<Customer> findByBankAccountId(BigDecimal bankAccountId);
//    //достать id счетов
//    List<Integer> findAllBankAccountIdsByPhoneNumber(String phoneNumber);
}