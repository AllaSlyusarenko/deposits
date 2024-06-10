package ru.mts.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "current_request_status", schema = "deposit")
public class CurrentRequestStatus { //2.4
    @EmbeddedId
    private CurrentRequestStatusId id = new CurrentRequestStatusId();

    @MapsId("requestId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Request idRequest;

    @MapsId("requestStatusId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "request_status_id", nullable = false)
    private RequestStatus idRequestStatus;

    @Column(name = "change_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime changeDateTime;

    public CurrentRequestStatus() {
    }

    @PrePersist
    protected void onCreate() {
        changeDateTime = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        changeDateTime = OffsetDateTime.now();
    }
}