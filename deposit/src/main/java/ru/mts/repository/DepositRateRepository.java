package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.DepositRate;

public interface DepositRateRepository extends JpaRepository<DepositRate, Integer> {
}
