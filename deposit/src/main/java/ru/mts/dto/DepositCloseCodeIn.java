package ru.mts.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Data
public class DepositCloseCodeIn {
    private String code;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime codeDateTime;
    private Integer idDeposit;

    public DepositCloseCodeIn() {
    }
}