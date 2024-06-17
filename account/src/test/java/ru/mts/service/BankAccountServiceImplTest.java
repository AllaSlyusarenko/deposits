package ru.mts.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.mts.entity.BankAccount;
import ru.mts.exception.NotFoundException;
import ru.mts.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName(value = "Тесты сервиса BankAccountService")
@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {
    private BankAccountService bankAccountService;
    @Mock
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    public void setUp() {
        bankAccountService = new BankAccountServiceImpl(bankAccountRepository);
    }

    @Test
    @DisplayName(value = "Проверить сумму для списания по несуществующему счету")
    @DirtiesContext
    void checkDataFromRequestSum_whenBankAccountNotFound() {
        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bankAccountService.checkDataFromRequestSum(new BigDecimal("5"), new BigDecimal("15000")));
        assertThat(exception.getMessage(), containsString("Bank Account Not Found"));
    }

    @Test
    @DisplayName(value = "Проверить сумму для списания по неактивному счету")
    @DirtiesContext
    void checkDataFromRequestSum_whenBankAccountNotActive() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccounts(1);
        bankAccount.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccount.setIsActive(false);
        bankAccount.setAmount(new BigDecimal("25000"));

        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.of(bankAccount));
        assertEquals(false, bankAccountService.checkDataFromRequestSum(new BigDecimal("5"), new BigDecimal("15000")));
    }

    @Test
    @DisplayName(value = "Проверить сумму для списания по активному счету с достаточной суммой")
    @DirtiesContext
    void checkDataFromRequestSum() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccounts(1);
        bankAccount.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccount.setIsActive(true);
        bankAccount.setAmount(new BigDecimal("25000"));

        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.of(bankAccount));
        assertEquals(true, bankAccountService.checkDataFromRequestSum(new BigDecimal("5"), new BigDecimal("15000")));
    }

    @Test
    @DisplayName(value = "Проверить получениe BankAccount по номеру счета")
    void getBankAccountByNumBankAccounts() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccounts(1);
        bankAccount.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccount.setIsActive(true);
        bankAccount.setAmount(new BigDecimal("25000"));
        List<BankAccount> bankAccounts = List.of(bankAccount);

        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.of(bankAccount));
        assertEquals(bankAccount, bankAccountService.getBankAccountByNumBankAccounts(new BigDecimal("51234567890123456789")));

    }

    @Test
    void reduceBalanceByNumBankAccounts() {
    }

    @Test
    void amountByIdBankAccounts() {
    }

    @Test
    void closeDeposit() {
    }

    @Test
    void createDepositAccount() {
    }

    @Test
    void createBankAccount() {
    }

    @Test
    void getBankAccountById() {
    }

    @Test
    void reduceBalance() {
    }

    @Test
    void getBankAccountByIdAccount() {
    }

    @Test
    void getBankAccountOutDtoByIdAccount() {
    }

    @Test
    void getBalance() {
    }

    @Test
    void increaseBalance() {
    }

    @Test
    void transferBalance() {
    }
}