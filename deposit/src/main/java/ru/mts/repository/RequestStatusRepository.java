package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.RequestStatus;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {
    RequestStatus findById(int id);
}
