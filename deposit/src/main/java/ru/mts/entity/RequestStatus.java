package ru.mts.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "idRequestStatus")
@Entity
@Table(name = "request_statuses", schema = "deposit")
public class RequestStatus { //2.5
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_statuses_generator")
    @SequenceGenerator(name = "request_statuses_generator", sequenceName = "deposit.id_request_status_sq", allocationSize = 1, initialValue = 1)
    private Integer idRequestStatus;
    @Column(name = "request_status_name")
    private String requestStatusName;

    public RequestStatus() {
    }

    @Override
    public String toString() {
        return requestStatusName;
    }
}