package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.Customer;
import ru.mts.exception.NotFoundException;
import ru.mts.exception.ValidationException;
import ru.mts.repository.CustomerRepository;

import java.math.BigDecimal;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getCustomerById(Integer id) {
        checkId(id);
        return customerRepository.findByIdCustomers(id).orElseThrow(()
                -> new NotFoundException("Клиент " + id + " не найден"));
    }

    @Override
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        checkPhoneNumber(phoneNumber);
        return customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(()
                -> new NotFoundException("Клиент с номером телефона " + phoneNumber + " не найден"));
    }

    @Override
    public Customer getCustomerByBankAccountId(BigDecimal bankAccountId) {
        checkBankAccountId(bankAccountId);
        return customerRepository.findByBankAccountId(bankAccountId).orElseThrow(()
                -> new NotFoundException("Клиент с номером счета " + bankAccountId + " не найден"));
    }

    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank() || phoneNumber.length() != 11) {
            throw new ValidationException("Неверный номер телефона " + phoneNumber);
        }
        return true;
    }

    private boolean checkBankAccountId(BigDecimal bankAccountId) {
        if (bankAccountId.toString().isBlank() || bankAccountId.toString().length() != 20) {
            throw new ValidationException("Неверный номер счета " + bankAccountId);
        }
        return true;
    }
}