package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.dto.BankAccountOutDto;
import ru.mts.entity.BankAccount;
import ru.mts.exception.NotFoundException;
import ru.mts.exception.ValidationException;
import ru.mts.mapper.BankAccountMapper;
import ru.mts.repository.BankAccountRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    // проверить есть ли на счету необходимая сумма
    @Override
    public Boolean checkDataFromRequestSum(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        BankAccount bankAccount =
                bankAccountRepository.findByNumBankAccounts(depositDebitingAccountId).orElseThrow(() -> new NotFoundException("Bank Account Not Found"));
        //проверить активен ли счет, у счета депозита есть момент, когда вклад закрыт и счет становится неактивен
        if (!bankAccount.getIsActive()) {
            return false;
        }
        return bankAccount.getAmount().subtract(depositAmount).compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public BankAccount createBankAccount(BigDecimal amount) {
        checkAmount(amount);
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setNumBankAccounts(randomNumBankAccount());
        bankAccountIn.setAmount(amount);
        return bankAccountRepository.save(bankAccountIn);
    }

    @Override
    public BankAccount getBankAccountById(Integer id) {
        checkId(id);
        return bankAccountRepository.findByIdBankAccounts(id).orElseThrow(()
                -> new NotFoundException("Банковский счет с id " + id + " не найден"));
    }

    //получить по idAccount - активные счета и их номер счета
    @Override
    public BigDecimal getBankAccountByIdAccount(Integer id) {
        checkId(id);
        BankAccount bankAccount = bankAccountRepository.findByIdBankAccounts(id).orElseThrow(()
                -> new NotFoundException("Банковский счет с id " + id + " не найден"));
        if (bankAccount.getIsActive().equals(true)) {
            return bankAccount.getNumBankAccounts();
        } else {
            throw new NotFoundException("Банковский счет с id " + id + " не активен");
        }
    }

    //    отдавать урезанную дто : номер и String счет, isActive
    //получить по id - активные счета и их номер счета
    @Override
    public BankAccountOutDto getBankAccountOutDtoByIdAccount(Integer id) {
        checkId(id);
        BankAccount bankAccount = bankAccountRepository.findByIdBankAccounts(id).orElseThrow(()
                -> new NotFoundException("Банковский счет с id " + id + " не найден"));
        return BankAccountMapper.bankAccountToDto(bankAccount);
    }


    @Override
    public BankAccount getBankAccountByNum(BigDecimal num) {
        return bankAccountRepository.findByNumBankAccounts(num).orElseThrow(()
                -> new NotFoundException("Банковский счет с р/с " + num + " не найден"));
    }

    @Override
    public BigDecimal getBalance(Integer id) {
        checkId(id);
        BankAccount bankAccount = getBankAccountById(id);
        return bankAccount.getAmount();
    }

    //увеличить баланс р/сч с id на сумму incAmount, вернуть увеличившийся итоговый баланс
    @Override
    public BankAccount increaseBalance(Integer id, BigDecimal incAmount) {
        checkId(id);
        checkAmount(incAmount);
        BankAccount bankAccount = getBankAccountById(id);
        BigDecimal newAmount = bankAccount.getAmount().add(incAmount);
        bankAccount.setAmount(newAmount);
        return bankAccountRepository.save(bankAccount);
    }

    //уменьшить баланс р/сч с id на сумму redAmount, проверка, вернуть уменьшившийся итоговый баланс
    @Override
    public BankAccount reduceBalance(Integer id, BigDecimal redAmount) {
        checkId(id);
        checkAmount(redAmount);
        BankAccount bankAccount = getBankAccountById(id);
        BigDecimal balance = getBalance(id);
        if (balance.subtract(redAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("На счете недостаточно средств, чтобы списать сумму " + redAmount);
        }
        bankAccount.setAmount(balance.subtract(redAmount));
        return bankAccountRepository.save(bankAccount);
    }

    //перевести с 1 на 2 сумму transferAmount, проверка, вернуть уменьшившийся итоговый баланс
    @Transactional
    @Override
    public BankAccount[] transferBalance(Integer from, Integer to, BigDecimal transferAmount) {
        checkId(from);
        checkId(to);
        checkAmount(transferAmount);
        BankAccount bankAccountFrom = reduceBalance(from, transferAmount);
        BankAccount bankAccountTo = increaseBalance(to, transferAmount);
        return new BankAccount[]{bankAccountFrom, bankAccountTo};
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

    private boolean checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Сумма должна быть больше нуля");
        }
        return true;
    }

    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }
}