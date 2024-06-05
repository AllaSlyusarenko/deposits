package ru.mts.service;

import ru.mts.entity.BankAccount;

import java.math.BigDecimal;

public interface BankAccountService {
    BankAccount createBankAccount(BigDecimal amount);
    BankAccount getBankAccount(int id);
    BankAccount getBankAccountByNum(String num);
}