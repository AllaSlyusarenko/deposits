package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.BankAccount;
import ru.mts.repository.BankAccountRepository;

import java.math.BigDecimal;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

//    public BankAccount test(){
//        BankAccount bankAccount2 = bankAccountRepository.findByNumBankAccounts(new BigDecimal( "41234567890123456789"));
//        return bankAccount2;
//    }

    @Override
    public BankAccount createBankAccount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setNumBankAccounts(randomNumBankAccount());
        bankAccountIn.setAmount(amount);
        BankAccount bankAccountOut = bankAccountRepository.save(bankAccountIn);
        return bankAccountOut;
    }

    @Override
    public BankAccount getBankAccount(int id) {
        return null;
    }

    @Override
    public BankAccount getBankAccountByNum(String id) {
        return null;
    }

    private BigDecimal randomNumBankAccount() {
        BigDecimal numBankAccount;
        do {
            BigDecimal randFromDouble = new BigDecimal(Math.random());
            BigDecimal randToDouble = new BigDecimal("99999999999999999988");
            numBankAccount = randFromDouble.multiply(randToDouble).setScale(0, BigDecimal.ROUND_HALF_UP);
        } while (bankAccountRepository.findByNumBankAccounts(numBankAccount).isPresent());
        return numBankAccount;
    }
}