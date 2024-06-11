package ru.mts.service;

import ru.mts.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer getCustomerById(Integer id);

    Customer getCustomerByPhoneNumber(String phoneNumber);

    Integer getIdByPhoneNumber(String phoneNumber);

    String sendEnterCode(String phoneNumber);

    boolean checkEnterCodeByPhoneNumber(String code, String phoneNumber);

    List<Integer> getAccountsByPhoneNumber(String phoneNumber);

}