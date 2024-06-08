package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {
}
