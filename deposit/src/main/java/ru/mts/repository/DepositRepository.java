package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.Deposit;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {
    List<Deposit> findAllByCustomerId(Integer customerId);
}
