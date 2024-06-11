package ru.mts.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mts.entity.DepositRate;
import ru.mts.entity.DepositsType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class RequestOutDto implements Serializable {
    private Integer idRequest;
    private DepositsType depositType; //снятие/пополнение
    private BigDecimal depositAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime createdDateTime;

    private DepositRate depositRate;


    public RequestOutDto() {
    }
}
