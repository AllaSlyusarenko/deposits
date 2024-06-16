package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.annotation.Logging;
import ru.mts.entity.BankAccountCustomer;
import ru.mts.repository.BankAccountCustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountCustomerService {
    private final BankAccountCustomerRepository bankAccountCustomerRepository;

    @Autowired
    public BankAccountCustomerService(BankAccountCustomerRepository bankAccountCustomerRepository) {
        this.bankAccountCustomerRepository = bankAccountCustomerRepository;
    }

    /**
     * Метод - найти id для получения списка всех счетов по idCustomer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public List<Integer> findAllByCustomerId(Integer customerId) {
        List<BankAccountCustomer> customers = bankAccountCustomerRepository.findAllByCustomerId(customerId);
        return customers.stream()
                .map(BankAccountCustomer::getBankAccountId).collect(Collectors.toList());
    }

    /**
     * Метод - для добавления счета вклада юзеру по idCustomer и счету вклада idDepositAccount
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Boolean addDepositAccountByIdAccount(Integer idCustomer, Integer idDepositAccount) {
        BankAccountCustomer bankAccountCustomer = new BankAccountCustomer();
        bankAccountCustomer.setCustomerId(idCustomer);
        bankAccountCustomer.setBankAccountId(idDepositAccount);
        BankAccountCustomer bankAccountCustomerSave = bankAccountCustomerRepository.save(bankAccountCustomer);
        return true;
    }

    /**
     * Метод - для получения списка активных счетов по idCustomer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public List<Integer> getAllAccounts(Integer idCustomer) {
        List<BankAccountCustomer> customers = bankAccountCustomerRepository.findAllByCustomerId(idCustomer);
        List<Integer> ids = customers.stream().map(BankAccountCustomer::getBankAccountId).collect(Collectors.toList());
        return ids;
    }
}