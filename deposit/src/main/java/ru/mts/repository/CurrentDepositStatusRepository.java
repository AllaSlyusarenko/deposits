package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.CurrentDepositStatus;

import java.util.Optional;

public interface CurrentDepositStatusRepository extends JpaRepository<CurrentDepositStatus, Integer> {
    Optional<CurrentDepositStatus> findByIdDeposit_IdDepositAndIdDepositStatus_IdDepositStatus(Integer idDeposit, Integer idStatus);
}
