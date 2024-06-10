package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.dto.BankAccountOutDto;
import ru.mts.entity.BankAccount;
import ru.mts.service.BankAccountService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/account")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create/{amount}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankAccount> createBankAccount(@PathVariable(value = "amount") @PositiveOrZero BigDecimal amount) {
        BankAccount bankAccount = bankAccountService.createBankAccount(amount);
        return new ResponseEntity<>(bankAccount, HttpStatus.CREATED);
    }

//    @GetMapping("/id/{id}")
//    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable(value = "id") @Positive Integer id) {
//        BankAccount bankAccount = bankAccountService.getBankAccountById(id);
//        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
//    }

    //получить по id - активные счета и их номер счета
    @GetMapping("/id/{id}")
    public ResponseEntity<BigDecimal> getBankAccountByIdAccount(@PathVariable(value = "id") @Positive Integer id) {
        //try catch
        BigDecimal bankAccount = bankAccountService.getBankAccountByIdAccount(id);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    //    отдавать урезанную дто : номер и String счет, isActive
    //получить по id - активные счета и их номер счета
    @GetMapping("/id/dto/{id}")
    public ResponseEntity<BankAccountOutDto> getBankAccountOutDtoByIdAccount(@PathVariable(value = "id") @Positive Integer id) {
        //try catch
        BankAccountOutDto bankAccount = bankAccountService.getBankAccountOutDtoByIdAccount(id);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }


    @GetMapping("/num/{num}")
    public ResponseEntity<BankAccount> getBankAccountByNum(@PathVariable(value = "num") @Positive BigDecimal num) {
        BankAccount bankAccount = bankAccountService.getBankAccountByNum(num);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    @GetMapping("/amount/{id}")
    public ResponseEntity<BigDecimal> getBalanceBankAccountById(@PathVariable(value = "id") @Positive Integer id) {
        BigDecimal amount = bankAccountService.getBalance(id);
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

    @PatchMapping("/increase/{id}/{incAmount}")
    public ResponseEntity<BankAccount> increaseBalanceBankAccountById(@PathVariable(value = "id") @Positive Integer id,
                                                                      @PathVariable(value = "incAmount") @Positive BigDecimal incAmount) {
        BankAccount bankAccount = bankAccountService.increaseBalance(id, incAmount);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    @PatchMapping("/reduce/{id}/{redAmount}")
    public ResponseEntity<BankAccount> reduceBalanceBankAccountById(@PathVariable(value = "id") @Positive Integer id,
                                                                    @PathVariable(value = "redAmount") @Positive BigDecimal redAmount) {
        BankAccount bankAccount = bankAccountService.reduceBalance(id, redAmount);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    @PatchMapping("/transfer/{from}/{transferAmount}/{to}")
    public ResponseEntity<BankAccount[]> transferBalanceBankAccountById(@PathVariable(value = "from") @Positive Integer from,
                                                                        @PathVariable(value = "transferAmount") @Positive BigDecimal transferAmount,
                                                                        @PathVariable(value = "to") @Positive Integer to) {
        BankAccount[] transfer = bankAccountService.transferBalance(from, to, transferAmount);
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

}