package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<Integer> findAllByCustomerId(Integer customerId) {
        List<BankAccountCustomer> customers = bankAccountCustomerRepository.findAllByCustomerId(customerId);
        return customers.stream()
                .map(BankAccountCustomer::getBankAccountId).collect(Collectors.toList());
    }

    //у юзера добавить счет вклада
    public Boolean addDepositAccountByIdAccount(Integer idCustomer, Integer idDepositAccount) {
        BankAccountCustomer bankAccountCustomer = new BankAccountCustomer();
        bankAccountCustomer.setCustomerId(idCustomer);
        bankAccountCustomer.setBankAccountId(idDepositAccount);
        BankAccountCustomer bankAccountCustomerSave = bankAccountCustomerRepository.save(bankAccountCustomer);
        return true;
    }
}