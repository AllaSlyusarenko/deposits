package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.CurrentDepositStatus;

public interface CurrentDepositStatusRepository extends JpaRepository<CurrentDepositStatus, Integer> {
}
