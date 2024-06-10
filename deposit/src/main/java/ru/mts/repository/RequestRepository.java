package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.Request;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Optional<Request> findFirstByCustomerIdOrderByIdRequestDesc(Integer idCustomer);
}
