package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.DepositCode;

public interface DepositCodeRepository extends JpaRepository<DepositCode, Integer> {

    DepositCode findFirstByIdDepositOrderByIdDepositCodeDesc(Integer idDeposit);
}