package ru.mts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.entity.CurrentRequestStatus;
import ru.mts.entity.Request;

import java.util.List;

public interface CurrentRequestStatusRepository extends JpaRepository<CurrentRequestStatus, Integer> {
    List<CurrentRequestStatus> findAllByIdRequest(Request idRequest);

    void deleteAllById_RequestId(Integer id);

}
