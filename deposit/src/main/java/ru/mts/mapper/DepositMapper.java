package ru.mts.mapper;

import lombok.experimental.UtilityClass;
import ru.mts.annotation.Logging;
import ru.mts.dto.DepositOutFullDto;
import ru.mts.dto.DepositOutShortDto;
import ru.mts.entity.Deposit;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DepositMapper {
    /**
     * Метод - для преобразования Deposit в DepositOutShortDto
     */
    @Logging(entering = true, exiting = true)
    public DepositOutShortDto toDepositOutShortDto(Deposit deposit) {
        DepositOutShortDto depositOutShortDto = new DepositOutShortDto();
        depositOutShortDto.setIdDeposit(deposit.getIdDeposit());
        depositOutShortDto.setDepositsType(deposit.getDepositsType().getDepositsTypesName());
        depositOutShortDto.setDepositAmount(deposit.getDepositAmount().toString());
        depositOutShortDto.setEndDate(deposit.getEndDate());
        depositOutShortDto.setDepositRate(deposit.getDepositRate().getDepositRate());
        return depositOutShortDto;
    }

    /**
     * Метод - для преобразования Deposit в DepositOutFullDto
     */
    @Logging(entering = true, exiting = true)
    public DepositOutFullDto toDepositOutFullDto(Deposit deposit) {
        DepositOutFullDto depositOutFullDto = new DepositOutFullDto();
        depositOutFullDto.setIdDeposit(deposit.getIdDeposit());
        depositOutFullDto.setDepositAccountId(deposit.getDepositAccountId());
        depositOutFullDto.setDepositTerm(deposit.getDepositTerm().getDepositTermName());
        depositOutFullDto.setDepositsType(deposit.getDepositsType().getDepositsTypesName());
        depositOutFullDto.setDepositAmount(deposit.getDepositAmount());
        depositOutFullDto.setDepositRate(deposit.getDepositRate().getDepositRate());
        depositOutFullDto.setTypesPercentPayment(deposit.getTypesPercentPayment().getTypePercentPaymentPeriod());
        depositOutFullDto.setPercentPaymentAccountId(deposit.getPercentPaymentAccountId());
        depositOutFullDto.setStartDate(deposit.getStartDate());
        depositOutFullDto.setEndDate(deposit.getEndDate());
        depositOutFullDto.setDepositRefundAccountId(deposit.getDepositRefundAccountId());
        return depositOutFullDto;
    }

    /**
     * Метод - для преобразования списка Deposit в список DepositOutShortDto
     */
    @Logging(entering = true, exiting = true)
    public List<DepositOutShortDto> toDepositOutShortDtos(List<Deposit> deposits) {
        return deposits.stream().map(DepositMapper::toDepositOutShortDto).collect(Collectors.toList());
    }
}