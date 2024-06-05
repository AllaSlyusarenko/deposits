package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.entity.BankAccount;
import ru.mts.service.BankAccountService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/account")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/create/{amount}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankAccount> createBankAccount(@PathVariable(value = "amount") BigDecimal amount) {
        BankAccount ba = bankAccountService.createBankAccount(amount);
        return new ResponseEntity<>(ba, HttpStatus.OK);
    }
}
