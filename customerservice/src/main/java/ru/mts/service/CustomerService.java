package ru.mts.service;

import ru.mts.entity.Customer;

import java.math.BigDecimal;

public interface CustomerService {
    Customer getCustomerById(Integer id);
    Customer getCustomerByPhoneNumber(String phoneNumber);
    Customer getCustomerByBankAccountId(BigDecimal bankAccountId);
    Integer getIdByPhoneNumber(String phoneNumber);
    Integer getIdByBankAccountId(BigDecimal bankAccountId);
}