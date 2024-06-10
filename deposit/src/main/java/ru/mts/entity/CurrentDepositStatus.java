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
@Table(name = "current_deposit_status", schema = "deposit")
public class CurrentDepositStatus { //2.11
    @EmbeddedId
    private CurrentDepositStatusId id = new CurrentDepositStatusId();

    @MapsId("depositId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deposit_id", nullable = false)
    private Deposit idDeposit;

    @MapsId("depositStatusId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "deposit_status_id", nullable = false)
    private DepositStatus idDepositStatus;

    @Column(name = "change_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime changeDateTime;

    @PrePersist
    protected void onCreate() {
        changeDateTime = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        changeDateTime = OffsetDateTime.now();
    }
}