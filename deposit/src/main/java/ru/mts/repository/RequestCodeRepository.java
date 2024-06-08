package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.RequestCode;

public interface RequestCodeRepository extends JpaRepository<RequestCode, Integer> {
    RequestCode findFirstByIdRequestOrderByIdRequestCodeDesc(Integer idRequest);
}
