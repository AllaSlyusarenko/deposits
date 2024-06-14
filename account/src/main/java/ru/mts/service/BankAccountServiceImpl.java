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
import java.util.List;
import java.util.Optional;

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

    //получить BankAccount по номеру р/сч
    @Override
    public BankAccount getBankAccountByNumBankAccounts(BigDecimal numBankAccounts) {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        BankAccount bankAccountOut = null;
        for (BankAccount bankAccount : bankAccounts) {
            if (numBankAccounts.equals(bankAccount.getNumBankAccounts())) {
                bankAccountOut = bankAccount;
            }
        }
//        return bankAccountRepository.findByNumBankAccounts(numBankAccounts).orElseThrow(()
//                -> new NotFoundException("Банковский счет с р/с " + numBankAccounts + " не найден"));
        return bankAccountOut;
    }

    //уменьшить баланс р/сч с NumBankAccounts на сумму depositAmount, проверка, вернуть уменьшившийся итоговый баланс
    @Override
    public BankAccount reduceBalanceByNumBankAccounts(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        BankAccount bankAccount = getBankAccountByNumBankAccounts(depositDebitingAccountId);
        BigDecimal balance = bankAccount.getAmount();
        if (balance.subtract(depositAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("На счете недостаточно средств, чтобы списать сумму " + depositAmount);
        }
        bankAccount.setAmount(balance.subtract(depositAmount));
        return bankAccountRepository.save(bankAccount);
    }

    //получить сумму на активном счете, если счет неактивный, то вернуть ноль
    @Override
    public BigDecimal amountByIdBankAccounts(Integer idBankAccounts) {
        BigDecimal amount;
        try {
            BankAccount bankAccounts = bankAccountRepository.findByIdBankAccountsAndIsActive(idBankAccounts, true).orElseThrow(()
                    -> new NotFoundException("Банковский счет с id " + idBankAccounts + " не найден"));
            amount = bankAccounts.getAmount();
        } catch (NotFoundException e) {
            amount = BigDecimal.ZERO;
        }
        return amount;
    }

    //закрыть для операций счет вклада и вернуть сумму на основной счет
    @Override
    public Boolean closeDeposit(BigDecimal depositAccountId, BigDecimal depositRefundAccountId, BigDecimal depositAmount) {
        //счет вклада
        BankAccount bankAccountDeposit = getBankAccountByNumBankAccounts(depositAccountId);
        bankAccountDeposit.setAmount(bankAccountDeposit.getAmount().subtract(depositAmount));
        bankAccountDeposit.setIsActive(false);
        bankAccountRepository.save(bankAccountDeposit);
        //основной счет
        BankAccount bankAccount  = getBankAccountByNumBankAccounts(depositRefundAccountId);
        bankAccount.setAmount(bankAccount.getAmount().add(depositAmount));
        bankAccountRepository.save(bankAccount);

        return true;
    }


    //создать банковский счет вклада с нужной суммой, эту сумму списать с основного счета
    @Override
    @Transactional
    public BankAccountOutDto createDepositAccount(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        BankAccount bankAccountDeposit = createBankAccount(depositAmount);
        //списать сумму с основного счета
        BankAccount bankAccountCustomer = reduceBalanceByNumBankAccounts(depositDebitingAccountId, depositAmount);
        BankAccountOutDto bankAccountOutDto = BankAccountMapper.bankAccountToDto(bankAccountDeposit);
        return bankAccountOutDto;
    }

    //создается вклад с передаваемой суммой
    @Override
    public BankAccount createBankAccount(BigDecimal amount) {
        checkAmount(amount);
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setNumBankAccounts(randomNumBankAccount());
        bankAccountIn.setAmount(amount);
        bankAccountIn.setIsActive(true); //активный
        return bankAccountRepository.save(bankAccountIn);
    }

    @Override
    public BankAccount getBankAccountById(Integer id) {
        checkId(id);
        return bankAccountRepository.findByIdBankAccounts(id).orElseThrow(()
                -> new NotFoundException("Банковский счет с id " + id + " не найден"));
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


    //получить по idAccount - активный банковский счет по idAccount и возвращает номер счета
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

    //отдавать урезанную дто : номер и String счет, isActive
    //получить по id - активные счета и их номер счета
    @Override
    public BankAccountOutDto getBankAccountOutDtoByIdAccount(Integer id) {
        checkId(id);
        BankAccount bankAccount = bankAccountRepository.findByIdBankAccounts(id).orElseThrow(()
                -> new NotFoundException("Банковский счет с id " + id + " не найден"));
        return BankAccountMapper.bankAccountToDto(bankAccount);
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

    // получить новый рандомный номер счета
    private BigDecimal randomNumBankAccount() {
        BigDecimal numBankAccount;
        do {
            BigDecimal randFromDouble = new BigDecimal(Math.random());
            BigDecimal randToDouble = new BigDecimal("99999999999999999988");
            numBankAccount = randFromDouble.multiply(randToDouble).setScale(0, BigDecimal.ROUND_HALF_UP);
        } while (bankAccountRepository.findByNumBankAccounts(numBankAccount).isPresent());
        return numBankAccount;
    }

    //проверка суммы списание/пополнение
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