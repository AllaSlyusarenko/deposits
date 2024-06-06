package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.mts.entity.Customer;
import ru.mts.repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerById(Integer id) {
        return null;
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return null;
    }
}
