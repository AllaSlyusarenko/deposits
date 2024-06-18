package ru.mts.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.mts.Main;
import ru.mts.TestDatabaseConfig;
import ru.mts.entity.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestDatabaseConfig.class, Main.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName(value = "BankAccount repository tests")
class BankAccountRepositoryTest {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    @DisplayName(value = "find all BankAccount")
    void findAllBankAccounts() {
        List<BankAccount> bankAccounts1 = bankAccountRepository.findAll();
        assertNotNull(bankAccounts1);
        assertEquals(1, bankAccounts1.size());
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumBankAccounts(new BigDecimal("51234567890123456789"));
        bankAccount.setIsActive(true);
        bankAccount.setAmount(new BigDecimal("25000"));
        bankAccountRepository.save(bankAccount);
        List<BankAccount> bankAccounts2 = bankAccountRepository.findAll();
        assertNotNull(bankAccounts2);
        assertEquals(2, bankAccounts2.size());
    }

    @Test
    @DisplayName(value = "find BankAccount by num BankAccount")
    void findByNumBankAccounts() {
        Optional<BankAccount> bankAccount = bankAccountRepository.findByNumBankAccounts(new BigDecimal("41234567890123456789"));
        bankAccount.ifPresent(account -> assertEquals(1, account.getIdBankAccounts()));
    }

    @Test
    @DisplayName(value = "find BankAccount by id BankAccount")
    void findByIdBankAccounts() {
        Optional<BankAccount> bankAccount = bankAccountRepository.findByIdBankAccounts(1);
        assertNotEquals(Optional.empty(), bankAccount);
        assertEquals(1, bankAccount.get().getIdBankAccounts());
        assertEquals(new BigDecimal("150000.00"), bankAccount.get().getAmount());
        assertEquals(true, bankAccount.get().getIsActive());
    }

    @Test
    @DisplayName(value = "find BankAccount by id BankAccount and active")
    void findByIdBankAccountsAndIsActive() {
        Optional<BankAccount> bankAccount1 = bankAccountRepository.findByIdBankAccountsAndIsActive(1, true);
        bankAccount1.ifPresent(account -> assertEquals(1, account.getIdBankAccounts()));
        Optional<BankAccount> bankAccount2 = bankAccountRepository.findByIdBankAccountsAndIsActive(1, false);
        assertEquals(Optional.empty(), bankAccount2);
    }
}