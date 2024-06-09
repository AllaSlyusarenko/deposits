package ru.mts.service;

import ru.mts.dto.EnterCodeIn;
import ru.mts.entity.Customer;

import java.math.BigDecimal;

public interface CustomerService {
    Customer getCustomerById(Integer id);
    Customer getCustomerByPhoneNumber(String phoneNumber);
    Customer getCustomerByBankAccountId(BigDecimal bankAccountId);
    Integer getIdByPhoneNumber(String phoneNumber);
    Integer getIdByBankAccountId(BigDecimal bankAccountId);
    String sendEnterCode(String phoneNumber);
//    boolean checkEnterCode(EnterCodeIn enterCodeIn);
    boolean checkEnterCodeByPhoneNumber(String code, String phoneNumber);
}