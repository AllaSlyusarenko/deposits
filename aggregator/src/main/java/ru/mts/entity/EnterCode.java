package ru.mts.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.PrePersist;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class EnterCode implements Serializable {

    private String code;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime;

    public EnterCode() {
    }
//
//    @PrePersist
//    protected void onCreate() {
//        codeDateTime = OffsetDateTime.now();
//    }
}