package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(exclude = "idDepositsTypes")
@Entity
@Table(name = "requests", schema = "deposit")
public class Request { //2.3
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_generator")
    @SequenceGenerator(name = "requests_generator", sequenceName = "deposit.id_request_sq", allocationSize = 1, initialValue = 1)
    private Integer idRequest;
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "request_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime requestDateTime;
    //    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_deposit")
//    private Deposit depositsId;
    @Column(name = "code")
    private String code;
    @Column(name = "code_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime;


    public Request() {
    }

    @PrePersist
    protected void onRequestDateTime() {
        requestDateTime = OffsetDateTime.now();
    }
}