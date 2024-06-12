package ru.mts.mapper;

import lombok.experimental.UtilityClass;
import ru.mts.dto.DepositOutShortDto;
import ru.mts.entity.Deposit;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DepositMapper {
    public DepositOutShortDto toDepositOutShortDto(Deposit deposit) {
        DepositOutShortDto depositOutShortDto = new DepositOutShortDto();
        depositOutShortDto.setDepositsType(deposit.getDepositsType().getDepositsTypesName());
        depositOutShortDto.setDepositAmount(deposit.getDepositAmount().toString());
        depositOutShortDto.setEndDate(deposit.getEndDate());
        depositOutShortDto.setDepositRate(deposit.getDepositRate().getDepositRate());
        return depositOutShortDto;
    }

    public List<DepositOutShortDto> toDepositOutShortDtos(List<Deposit> deposits) {
        return deposits.stream().map(DepositMapper::toDepositOutShortDto).collect(Collectors.toList());
    }
}