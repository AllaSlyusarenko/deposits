package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.dto.EnterCodeIn;
import ru.mts.entity.Customer;
import ru.mts.service.CustomerService;
import ru.mts.service.EnterCodeServiceImpl;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;
    private EnterCodeServiceImpl enterCodeService;

    @Autowired
    public CustomerController(CustomerService customerService, EnterCodeServiceImpl enterCodeService) {
        this.customerService = customerService;
        this.enterCodeService = enterCodeService;
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

    @GetMapping("/phone/id/{phoneNumber}")
    public ResponseEntity<Integer> getIdByByPhoneNumber(@PathVariable(value = "phoneNumber") String phoneNumber) {
        Integer id = customerService.getIdByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/bankaccount/id/{bankAccountId}")
    public ResponseEntity<Integer> getIdByByBankAccountId(@PathVariable(value = "bankAccountId") BigDecimal bankAccountId) {
        Integer id = customerService.getIdByBankAccountId(bankAccountId);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/code/id/{customerId}")
    public ResponseEntity<String> getLastEnterCodeByIdCustomer(@PathVariable(value = "customerId") Integer customerId) {
        String code = enterCodeService.getLastEnterCodeByIdCustomer(customerId);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @GetMapping("/codedatetime/id/{customerId}")
    public ResponseEntity<OffsetDateTime> getLastEnterCodeDateTimeByIdCustomer(@PathVariable(value = "customerId") Integer customerId) {
        OffsetDateTime codeDateTime = enterCodeService.getLastEnterCodeDateTimeByIdCustomer(customerId);
        return new ResponseEntity<>(codeDateTime, HttpStatus.OK);
    }

    //отправить смс по телефону
    @GetMapping("/sendcode/{phoneNumber}")
    public ResponseEntity<String> sendCode(@PathVariable(value = "phoneNumber") String phoneNumber) {
        String message = customerService.sendEnterCode(phoneNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //проверить смс код по customerId
    @GetMapping("/checkcode/{customerId}")
    public ResponseEntity<Boolean> checkCode(@PathVariable(value = "customerId") Integer customerId,
                                           @RequestBody EnterCodeIn enterCodeIn) {
        Boolean isOk = customerService.checkEnterCode(enterCodeIn);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }
}