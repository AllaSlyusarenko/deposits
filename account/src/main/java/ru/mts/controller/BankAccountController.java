package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.annotation.Logging;
import ru.mts.dto.BankAccountOutDto;
import ru.mts.entity.BankAccount;
import ru.mts.service.BankAccountService;

import javax.validation.constraints.Positive;
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

    /**
     * Метод - для уменьшения суммы счета по номеру счета depositDebitingAccountId на сумму depositAmount
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/reduceBalance/{depositDebitingAccountId}/{depositAmount}")
    public ResponseEntity<BankAccount> reduceBalanceByNumBankAccounts(@PathVariable(value = "depositDebitingAccountId") BigDecimal depositDebitingAccountId,
                                                                      @PathVariable(value = "depositAmount") BigDecimal depositAmount) {
        try {
            BankAccount amount = bankAccountService.reduceBalanceByNumBankAccounts(depositDebitingAccountId, depositAmount);
            return new ResponseEntity<>(amount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Метод - для получения по idAccount - активный банковский счет по idAccount и возвращает номер счета
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/id/{id}")
    public ResponseEntity<BigDecimal> getBankAccountByIdAccount(@PathVariable(value = "id") @Positive Integer id) {
        BigDecimal bankAccount = bankAccountService.getBankAccountByIdAccount(id);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    /**
     * Метод - для получения по id - активные счета и их номер счета, отдает урезанную дто: номер и String счет, isActive
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/id/dto/{id}")
    public ResponseEntity<BankAccountOutDto> getBankAccountOutDtoByIdAccount(@PathVariable(value = "id") @Positive Integer id) {
        try {
            BankAccountOutDto bankAccount = bankAccountService.getBankAccountOutDtoByIdAccount(id);
            return new ResponseEntity<>(bankAccount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Метод - для получения BankAccount по номеру счета
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/num/{num}")
    public ResponseEntity<BankAccount> getBankAccountByNum(@PathVariable(value = "num") @Positive BigDecimal num) {
        BankAccount bankAccount = bankAccountService.getBankAccountByNumBankAccounts(num);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    /**
     * Метод - для получения суммы по id счета
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/amount/{id}")
    public ResponseEntity<BigDecimal> getBalanceBankAccountById(@PathVariable(value = "id") @Positive Integer id) {
        BigDecimal amount = bankAccountService.getBalance(id);
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

    /**
     * Метод - для увеличения суммы по id счета на сумму incAmount
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @PatchMapping("/increase/{id}/{incAmount}")
    public ResponseEntity<BankAccount> increaseBalanceBankAccountById(@PathVariable(value = "id") @Positive Integer id,
                                                                      @PathVariable(value = "incAmount") @Positive BigDecimal incAmount) {
        BankAccount bankAccount = bankAccountService.increaseBalance(id, incAmount);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    /**
     * Метод - для уменьшения суммы по id счета на сумму incAmount
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @PatchMapping("/reduce/{id}/{redAmount}")
    public ResponseEntity<BankAccount> reduceBalanceBankAccountById(@PathVariable(value = "id") @Positive Integer id,
                                                                    @PathVariable(value = "redAmount") @Positive BigDecimal redAmount) {
        BankAccount bankAccount = bankAccountService.reduceBalance(id, redAmount);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    /**
     * Метод - для перевода суммы transferAmount по id со счета from на to
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @PatchMapping("/transfer/{from}/{transferAmount}/{to}")
    public ResponseEntity<BankAccount[]> transferBalanceBankAccountById(@PathVariable(value = "from") @Positive Integer from,
                                                                        @PathVariable(value = "transferAmount") @Positive BigDecimal transferAmount,
                                                                        @PathVariable(value = "to") @Positive Integer to) {
        BankAccount[] transfer = bankAccountService.transferBalance(from, to, transferAmount);
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    /**
     * Метод - для проверки есть ли на счету необходимая сумма
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/checksum/{depositDebitingAccountId}/{depositAmount}")
    public ResponseEntity<Boolean> checkDataFromRequestSum(
            @PathVariable(value = "depositDebitingAccountId") @Positive BigDecimal depositDebitingAccountId,
            @PathVariable(value = "depositAmount") @Positive BigDecimal depositAmount) {
        try {
            Boolean isOk = bankAccountService.checkDataFromRequestSum(depositDebitingAccountId, depositAmount);
            return new ResponseEntity<>(isOk, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Метод - для создания нового активного счета вклада и сразу перечислить туда сумму со счета depositDebitingAccountId
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/createdepaccount/{depositDebitingAccountId}/{depositAmount}")
    public ResponseEntity<BankAccountOutDto> createDepositAccount( //возвращает созданный номер вклада
                                                                   @PathVariable(value = "depositDebitingAccountId") BigDecimal depositDebitingAccountId,
                                                                   @PathVariable(value = "depositAmount") BigDecimal depositAmount) {
        try {
            BankAccountOutDto account = bankAccountService.createDepositAccount(depositDebitingAccountId, depositAmount);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Метод - для получения суммы на активном счете, если счет неактивный, то вернуть ноль
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/amountbyid/{idBankAccounts}")
    public ResponseEntity<BigDecimal> amountByIdBankAccounts(@PathVariable(value = "idBankAccounts") Integer idBankAccounts) {
        try {
            BigDecimal amount = bankAccountService.amountByIdBankAccounts(idBankAccounts);
            return new ResponseEntity<>(amount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Метод - при закрытии вклада => счет вклада сделать неактивным, сумму перевести на основной счет
     */
    @Logging(entering = true, exiting = true, logResult = true)
    @GetMapping("/closedeposit/{depositAccountId}/{depositRefundAccountId}/{depositAmount}")
    public ResponseEntity<Boolean> closeDeposit(@PathVariable(value = "depositAccountId") BigDecimal depositAccountId,
                                                @PathVariable(value = "depositRefundAccountId") BigDecimal depositRefundAccountId,
                                                @PathVariable(value = "depositAmount") BigDecimal depositAmount) {
        try {
            Boolean isOk = bankAccountService.closeDeposit(depositAccountId, depositRefundAccountId, depositAmount);
            return new ResponseEntity<>(isOk, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}