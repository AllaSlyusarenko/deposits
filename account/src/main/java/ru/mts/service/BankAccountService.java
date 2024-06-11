package ru.mts.service;

import ru.mts.dto.BankAccountOutDto;
import ru.mts.entity.BankAccount;

import java.math.BigDecimal;

public interface BankAccountService {
    BankAccount createBankAccount(BigDecimal amount);

    BankAccount getBankAccountById(Integer id);

    BankAccount getBankAccountByNum(BigDecimal num);

    BigDecimal getBalance(Integer id);

    BankAccount increaseBalance(Integer id, BigDecimal incAmount);

    BankAccount reduceBalance(Integer id, BigDecimal incAmount);

    BankAccount[] transferBalance(Integer from, Integer to, BigDecimal transferAmount);
    BigDecimal getBankAccountByIdAccount(Integer id);
    BankAccountOutDto getBankAccountOutDtoByIdAccount(Integer id);
    Boolean checkDataFromRequestSum(BigDecimal depositDebitingAccountId, BigDecimal depositAmount);
}