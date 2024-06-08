package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.CurrentRequestStatus;

public interface CurrentRequestStatusRepository extends JpaRepository<CurrentRequestStatus, Integer> {
}
