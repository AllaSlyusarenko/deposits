package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.annotation.Logging;
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

    /**
     * Метод - для проверки есть ли на счету необходимая сумма
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public Boolean checkDataFromRequestSum(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        checkAmount(depositAmount);
        BankAccount bankAccount =
                bankAccountRepository.findByNumBankAccounts(depositDebitingAccountId).orElseThrow(() -> new NotFoundException("Bank Account Not Found"));
        //проверить активен ли счет, у счета депозита есть момент, когда вклад закрыт и счет становится неактивен
        if (!bankAccount.getIsActive()) {
            return false;
        }
        return bankAccount.getAmount().subtract(depositAmount).compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Метод - для получения BankAccount по номеру счета
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccount getBankAccountByNumBankAccounts(BigDecimal numBankAccounts) {
        BankAccount bankAccount = bankAccountRepository.findByNumBankAccounts(numBankAccounts)
                .orElseThrow(() -> new NotFoundException("Bank Account Not Found"));
        return bankAccount;

    }

    /**
     * Метод - для уменьшения суммы счета по номеру счета depositDebitingAccountId на сумму depositAmount
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccount reduceBalanceByNumBankAccounts(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        checkAmount(depositAmount);
        BankAccount bankAccount = getBankAccountByNumBankAccounts(depositDebitingAccountId);
        BigDecimal balance = bankAccount.getAmount();
        if (balance.subtract(depositAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("На счете недостаточно средств, чтобы списать сумму " + depositAmount);
        }
        bankAccount.setAmount(balance.subtract(depositAmount));
        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Метод - для получения суммы на активном счете, если счет неактивный, то вернуть ноль
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - при закрытии вклада => счет вклада сделать неактивным, сумму перевести на основной счет
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public Boolean closeDeposit(BigDecimal depositAccountId, BigDecimal depositRefundAccountId, BigDecimal depositAmount) {
        //счет вклада
        closeAccountDeposit(depositAccountId, depositAmount);
        //основной счет
        refundOfAmount(depositRefundAccountId, depositAmount);
        return true;
    }

    /**
     * Метод - при закрытии вклада => счет вклада сделать неактивным, списать сумму
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccount closeAccountDeposit(BigDecimal depositAccountId, BigDecimal depositAmount) {
        //счет вклада
        BankAccount bankAccountDeposit = getBankAccountByNumBankAccounts(depositAccountId);
        bankAccountDeposit.setAmount(bankAccountDeposit.getAmount().subtract(depositAmount));
        bankAccountDeposit.setIsActive(false);
        return bankAccountRepository.save(bankAccountDeposit);
    }

    /**
     * Метод - при закрытии вклада => перевести сумму на счет для возврата суммы
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccount refundOfAmount(BigDecimal depositRefundAccountId, BigDecimal depositAmount) {
        //основной счет
        BankAccount bankAccount = getBankAccountByNumBankAccounts(depositRefundAccountId);
        bankAccount.setAmount(bankAccount.getAmount().add(depositAmount));
        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Метод - для создания нового активного счета вклада и сразу перечислить туда сумму со счета depositDebitingAccountId
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    @Transactional
    public BankAccountOutDto createDepositAccount(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        checkAmount(depositAmount);
        BankAccount bankAccountDeposit = createBankAccount(depositAmount);
        //списать сумму с основного счета
        BankAccount bankAccountCustomer = reduceBalanceByNumBankAccounts(depositDebitingAccountId, depositAmount);
        BankAccountOutDto bankAccountOutDto = BankAccountMapper.bankAccountToDto(bankAccountDeposit);
        return bankAccountOutDto;
    }

    /**
     * Метод - для создания вклад с передаваемой суммой
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccount createBankAccount(BigDecimal amount) {
        checkAmount(amount);
        BankAccount bankAccountIn = new BankAccount();
        bankAccountIn.setNumBankAccounts(randomNumBankAccount());
        bankAccountIn.setAmount(amount);
        bankAccountIn.setIsActive(true); //активный
        return bankAccountRepository.save(bankAccountIn);
    }

    /**
     * Метод - для получения BankAccount по id
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccount getBankAccountById(Integer id) {
        checkId(id);
        return bankAccountRepository.findByIdBankAccounts(id).orElseThrow(()
                -> new NotFoundException("Банковский счет с id " + id + " не найден"));
    }

    /**
     * Метод - для уменьшения суммы по id счета на сумму redAmount, проверка суммы, вернуть уменьшившийся итоговый баланс
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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


    /**
     * Метод - для получения по idAccount - активный банковский счет по idAccount и возвращает номер счета
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - для получения по id - активные счета и их номер счета, отдает урезанную дто: номер и String счет, isActive
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccountOutDto getBankAccountOutDtoByIdAccount(Integer id) {
        checkId(id);
        BankAccount bankAccount = bankAccountRepository.findByIdBankAccounts(id).orElseThrow(()
                -> new NotFoundException("Банковский счет с id " + id + " не найден"));
        return BankAccountMapper.bankAccountToDto(bankAccount);
    }

    /**
     * Метод - для получения суммы по id счета
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BigDecimal getBalance(Integer id) {
        checkId(id);
        BankAccount bankAccount = getBankAccountById(id);
        return bankAccount.getAmount();
    }

    /**
     * Метод - для увеличения суммы по id счета на сумму incAmount
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    @Override
    public BankAccount increaseBalance(Integer id, BigDecimal incAmount) {
        checkId(id);
        checkAmount(incAmount);
        BankAccount bankAccount = getBankAccountById(id);
        BigDecimal newAmount = bankAccount.getAmount().add(incAmount);
        bankAccount.setAmount(newAmount);
        return bankAccountRepository.save(bankAccount);
    }


    /**
     * Метод - для перевода суммы transferAmount по id со счета from на to
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - для получения нового рандомного номера счета
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    private BigDecimal randomNumBankAccount() {
        BigDecimal numBankAccount;
        BigDecimal min = new BigDecimal("10000000000000000000");
        BigDecimal max = new BigDecimal("99999999999999999999");
        do {
            BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
            numBankAccount = randomBigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
        } while (bankAccountRepository.findByNumBankAccounts(numBankAccount).isPresent());
        return numBankAccount;
    }

    /**
     * Метод - для проверки суммы
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    private boolean checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Сумма должна быть больше нуля");
        }
        return true;
    }

    /**
     * Метод - для проверки id
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }
}