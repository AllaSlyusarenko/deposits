package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.entity.Customer;
import ru.mts.service.CustomerService;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") @Positive Integer id) {
        Customer customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<Customer> getCustomerByPhoneNumber(@PathVariable(value = "phoneNumber") String phoneNumber) {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/bankaccount/{bankAccountId}")
    public ResponseEntity<Customer> getCustomerByBankAccountId(@PathVariable(value = "bankAccountId") BigDecimal bankAccountId) {
        Customer customer = customerService.getCustomerByBankAccountId(bankAccountId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}