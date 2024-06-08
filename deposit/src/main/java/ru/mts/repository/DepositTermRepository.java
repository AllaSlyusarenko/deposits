package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.DepositTerm;

public interface DepositTermRepository extends JpaRepository<DepositTerm, Integer> {
    DepositTerm findDepositTermByDepositTermName(String depositTermName);
}