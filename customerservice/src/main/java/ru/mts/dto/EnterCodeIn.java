package ru.mts.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class EnterCodeIn implements Serializable {
    private String code;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime;
    private Integer idCustomer;

    public EnterCodeIn() {
    }

    @PrePersist
    protected void onCreate() {
        codeDateTime = OffsetDateTime.now();
    }
}