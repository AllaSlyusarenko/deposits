package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.DepositsType;

public interface DepositsTypeRepository extends JpaRepository<DepositsType, Integer> {
}
