package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.Deposit;
import ru.mts.exception.ValidationException;
import ru.mts.repository.DepositRepository;

import java.util.List;

@Service
public class DepositServiceImpl {
    private final DepositRepository depositRepository;

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

//    //получить все активные депозиты по idCustomer
//    public List<Deposit> getAllByIdCustomerActiveDeposits(Integer idCustomer) {
//
//    }

    //проверка id
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }


}