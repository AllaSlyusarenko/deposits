package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.TypesPercentPayment;

public interface TypesPercentPaymentRepository extends JpaRepository<TypesPercentPayment, Integer> {
    TypesPercentPayment findTypesPercentPaymentByTypePercentPaymentPeriod (String typePercentPaymentPeriod);
}
