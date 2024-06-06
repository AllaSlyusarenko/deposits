package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}