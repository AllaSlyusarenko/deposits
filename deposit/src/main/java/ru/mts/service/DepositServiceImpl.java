package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.Deposit;
import ru.mts.entity.Request;
import ru.mts.exception.ValidationException;
import ru.mts.repository.DepositRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DepositServiceImpl {
    private final DepositRepository depositRepository;
    private final RequestServiceImpl requestService;

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository, RequestServiceImpl requestService) {
        this.depositRepository = depositRepository;
        this.requestService = requestService;
    }
    //создает вклад по заявке idRequest
    //вернуть дто для отображения в удачном сообщении, сделать депозит и оттуда взять ставку и вид вклада
    public Deposit createDepositByIdRequest(Integer idCustomer, Integer idRequest, BigDecimal numBankAccounts){
        Request request = requestService.getRequestById(idRequest);
        Deposit deposit = new Deposit();
        deposit.setRequest(request);
        deposit.setCustomerId(idCustomer);
        deposit.setDepositRefill(request.isDepositRefill());
        deposit.setReductionOfDeposit(request.isReductionOfDeposit());
        deposit.setDepositsType(); //написать метод на определение вида вклада - входные из заявки
        deposit.setStartDate(request.getRequestDateTime());
        deposit.setDepositTerm(request.getDepositTerm());
        deposit.setEndDate(); //написать метод на определение конца вклада - старт + срок + 1 день
        deposit.setDepositAmount(request.getDepositAmount());
        deposit.setDepositRate();  //написать метод на определение ставки от: вид, срок, сумма
        deposit.setTypesPercentPayment(request.getTypesPercentPayment());
        deposit.setDepositAccountId(numBankAccounts); // счет депозита
        deposit.setDepositDebitingAccountId(request.getDepositDebitingAccountId()); //счет для списания суммы депозита
        deposit.setPercentPaymentAccountId(request.getPercentPaymentAccountId()); //счет для выплаты процентов
        deposit.setDepositRefundAccountId(request.getDepositRefundAccountId()); //счет для возвращения вклада
        deposit.setActive(true); //вклад активный, отображать, снимать, пополнять - от условий

        return depositRepository.save(deposit);

    }

    //метод, который возвращает дто для отображения



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