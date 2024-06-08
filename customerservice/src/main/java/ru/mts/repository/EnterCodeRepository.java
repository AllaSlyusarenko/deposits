package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.EnterCode;

public interface EnterCodeRepository extends JpaRepository<EnterCode, Integer> {
    EnterCode findFirstByIdCustomerOrderByIdEnterCodeDesc(Integer customerId);
}