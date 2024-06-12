package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.DepositStatus;

public interface DepositStatusRepository extends JpaRepository<DepositStatus, Integer> {
    DepositStatus findById(int id);
}