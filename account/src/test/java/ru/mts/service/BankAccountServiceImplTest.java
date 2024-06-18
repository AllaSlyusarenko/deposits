package ru.mts.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.mts.dto.BankAccountOutDto;
import ru.mts.entity.BankAccount;
import ru.mts.exception.NotFoundException;
import ru.mts.exception.ValidationException;
import ru.mts.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.of(bankAccount));
        assertEquals(bankAccount, bankAccountService.getBankAccountByNumBankAccounts(new BigDecimal("51234567890123456789")));

    }

    @Test
    @DisplayName(value = "Списание суммы по номеру счета, получениe BankAccount")
    void reduceBalanceByNumBankAccounts() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("25000"));
        BankAccount bankAccountSave = new BankAccount();
        bankAccountSave.setIdBankAccounts(1);
        bankAccountSave.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccountSave.setIsActive(true);
        bankAccountSave.setAmount(new BigDecimal("15000"));

        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.of(bankAccountIn));
        when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(bankAccountSave);
        assertEquals(bankAccountSave, bankAccountService.reduceBalanceByNumBankAccounts(new BigDecimal("51234567890123456789"), new BigDecimal("10000.00")));
    }

    @Test
    @DisplayName(value = "Списание суммы по номеру счета, неверная сумма")
    void reduceBalanceByNumBankAccounts_WrongAmount() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bankAccountService.reduceBalanceByNumBankAccounts(new BigDecimal("51234567890123456789"), new BigDecimal("-10000.00")));
        assertThat(exception.getMessage(), containsString("Сумма должна быть больше нуля"));
    }

    @Test
    @DisplayName(value = "получение суммы на активном счете, если счет неактивный, то вернуть ноль")
    void amountByIdBankAccounts_ActiveDeposit() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccounts(1);
        bankAccount.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccount.setIsActive(true);
        bankAccount.setAmount(new BigDecimal("25000"));
        when(bankAccountRepository.findByIdBankAccountsAndIsActive(Mockito.anyInt(), Mockito.any(Boolean.class))).thenReturn(Optional.of(bankAccount));
        assertEquals(new BigDecimal("25000"), bankAccountService.amountByIdBankAccounts(bankAccount.getIdBankAccounts()));
    }

    @Test
    @DisplayName(value = "получение суммы на неактивном счете то вернуть ноль")
    void amountByIdBankAccounts_NotActiveDeposit() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccounts(1);
        bankAccount.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccount.setIsActive(false);
        bankAccount.setAmount(new BigDecimal("25000"));
        when(bankAccountRepository.findByIdBankAccountsAndIsActive(Mockito.anyInt(), Mockito.any(Boolean.class))).thenReturn(Optional.empty());
        assertEquals(new BigDecimal("0"), bankAccountService.amountByIdBankAccounts(bankAccount.getIdBankAccounts()));
    }

    @Test
    @DisplayName(value = "при закрытии вклада => счет вклада сделать неактивным, списать сумму")
    void closeAccountDeposit() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("25000"));
        BankAccount bankAccountSave = new BankAccount();
        bankAccountSave.setIdBankAccounts(1);
        bankAccountSave.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccountSave.setIsActive(false);
        bankAccountSave.setAmount(new BigDecimal("0"));

        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.of(bankAccountIn));
        when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(bankAccountSave);
        assertEquals(bankAccountSave, bankAccountService.closeAccountDeposit(new BigDecimal("51234567890123456789"), new BigDecimal("25000")));
    }

    @Test
    @DisplayName(value = "при закрытии вклада => перевести сумму на счет для возврата суммы")
    void refundOfAmount() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("35000"));
        BankAccount bankAccountSave = new BankAccount();
        bankAccountSave.setIdBankAccounts(1);
        bankAccountSave.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountSave.setIsActive(true);
        bankAccountSave.setAmount(new BigDecimal("45000"));

        when(bankAccountRepository.findByNumBankAccounts(Mockito.any(BigDecimal.class))).thenReturn(Optional.of(bankAccountIn));
        when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(bankAccountSave);
        assertEquals(bankAccountSave, bankAccountService.closeAccountDeposit(new BigDecimal("61234567890123456789"), new BigDecimal("10000")));
    }

    @Test
    @DisplayName(value = "при закрытии вклада => перевести неверную сумму на счет для возврата суммы")
    void createDepositAccount_IncorrectAmount() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bankAccountService.createDepositAccount(new BigDecimal("51234567890123456789"), new BigDecimal("-10000.00")));
        assertThat(exception.getMessage(), containsString("Сумма должна быть больше нуля"));

    }

    @Test
    @DisplayName(value = "при закрытии вклада => перевести сумму на счет для возврата суммы")
    void createDepositAccount_CorrectAmount() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bankAccountService.createDepositAccount(new BigDecimal("51234567890123456789"), new BigDecimal("-10000.00")));
        assertThat(exception.getMessage(), containsString("Сумма должна быть больше нуля"));
    }

    @Test
    @DisplayName(value = "для создания вклада с передаваемой суммой")
    void createBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccounts(1);
        bankAccount.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccount.setIsActive(true);
        bankAccount.setAmount(new BigDecimal("10000"));

        when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(bankAccount);
        assertEquals(new BigDecimal("10000"), bankAccountService.createBankAccount(new BigDecimal("10000")).getAmount());
    }

    @Test
    @DisplayName(value = "для получения BankAccount по неверному id")
    void getBankAccountById_IncorrectId() {
        Integer id = 5;

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bankAccountService.getBankAccountById(id));
        assertThat(exception.getMessage(), containsString("Банковский счет с id " + id + " не найден"));
    }

    @Test
    @DisplayName(value = "для получения BankAccount по верному id")
    void getBankAccountById_CorrectId() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccounts(1);
        bankAccount.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccount.setIsActive(true);
        bankAccount.setAmount(new BigDecimal("10000"));

        when(bankAccountRepository.findByIdBankAccounts(Mockito.anyInt())).thenReturn(Optional.of(bankAccount));
        assertEquals(bankAccount, bankAccountService.getBankAccountById(1));
    }

    @Test
    @DisplayName(value = "для уменьшения суммы по id счета на сумму redAmount")
    void reduceBalance() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("25000"));
        BankAccount bankAccountSave = new BankAccount();
        bankAccountSave.setIdBankAccounts(1);
        bankAccountSave.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountSave.setIsActive(true);
        bankAccountSave.setAmount(new BigDecimal("15000"));

        when(bankAccountRepository.findByIdBankAccounts(Mockito.anyInt())).thenReturn(Optional.of(bankAccountIn));
        when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(bankAccountSave);

        assertEquals(bankAccountSave, bankAccountService.reduceBalance(1, new BigDecimal("10000")));
    }

    @Test
    @DisplayName(value = "для уменьшения суммы по id счета на сумму redAmount, недостаточно средств")
    void reduceBalance_HighAmount() {
        BigDecimal redAmount = new BigDecimal("100000");
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("25000"));

        when(bankAccountRepository.findByIdBankAccounts(Mockito.anyInt())).thenReturn(Optional.of(bankAccountIn));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bankAccountService.reduceBalance(bankAccountIn.getIdBankAccounts(), redAmount));
        assertThat(exception.getMessage(), containsString("На счете недостаточно средств, чтобы списать сумму " + redAmount));
    }

    @Test
    @DisplayName(value = "для получения по idAccount - активный банковский счет по idAccount")
    void getBankAccountByIdAccount() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("25000"));

        when(bankAccountRepository.findByIdBankAccounts(Mockito.anyInt())).thenReturn(Optional.of(bankAccountIn));
        assertEquals(new BigDecimal("61234567890123456789"), bankAccountService.getBankAccountByIdAccount(1));
    }

    @Test
    @DisplayName(value = "для получения по idAccount - неактивный банковский счет по idAccount")
    void getBankAccountByIdAccount_NotActive() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountIn.setIsActive(false);
        bankAccountIn.setAmount(new BigDecimal("25000"));

        when(bankAccountRepository.findByIdBankAccounts(Mockito.anyInt())).thenReturn(Optional.of(bankAccountIn));
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bankAccountService.getBankAccountByIdAccount(1));
        assertThat(exception.getMessage(), containsString("Банковский счет с id " + 1 + " не активен"));
    }

    @Test
    @DisplayName(value = "проверка маппера, для получения dto по id вклада")
    void getBankAccountOutDtoByIdAccount() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("25000"));

        BankAccountOutDto dto = new BankAccountOutDto();
        dto.setIdBankAccounts(1);
        dto.setNumBankAccounts("61234567890123456789");
        dto.setIsActive(true);

        when(bankAccountRepository.findByIdBankAccounts(Mockito.anyInt())).thenReturn(Optional.of(bankAccountIn));
        assertEquals(dto, bankAccountService.getBankAccountOutDtoByIdAccount(1));
    }

    @Test
    @DisplayName(value = "для увеличения суммы по id счета на сумму incAmount")
    void increaseBalance() {
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setIdBankAccounts(1);
        bankAccountIn.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountIn.setIsActive(true);
        bankAccountIn.setAmount(new BigDecimal("25000"));
        BankAccount bankAccountSave = new BankAccount();
        bankAccountSave.setIdBankAccounts(1);
        bankAccountSave.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountSave.setIsActive(true);
        bankAccountSave.setAmount(new BigDecimal("35000"));

        when(bankAccountRepository.findByIdBankAccounts(Mockito.anyInt())).thenReturn(Optional.of(bankAccountIn));
        when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(bankAccountSave);

        assertEquals(bankAccountSave, bankAccountService.increaseBalance(1, new BigDecimal("10000")));
    }
}