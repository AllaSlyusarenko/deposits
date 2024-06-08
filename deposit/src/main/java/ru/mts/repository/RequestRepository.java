package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
