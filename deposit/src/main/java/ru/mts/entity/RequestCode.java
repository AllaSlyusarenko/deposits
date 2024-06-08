package ru.mts.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(exclude = "idRequestCode")
@Entity
@Table(name = "request_code", schema = "deposit")
public class RequestCode { //2.17
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_code_generator")
    @SequenceGenerator(name = "request_code_generator", sequenceName = "deposit.id_request_code_sq", allocationSize = 1, initialValue = 1)
    private Integer idRequestCode;
    @Column(name = "code")
    private String code;
    @Column(name = "code_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime;
    @Column(name = "id_request")
    private Integer idRequest;

    public RequestCode() {
    }

    @Override
    public String toString() {
        return "RequestCode {" +
                "idRequestCode=" + idRequestCode +
                ", code='" + code + '\'' +
                ", codeDateTime=" + codeDateTime +
                ", idRequest=" + idRequest +
                '}';
    }
}
