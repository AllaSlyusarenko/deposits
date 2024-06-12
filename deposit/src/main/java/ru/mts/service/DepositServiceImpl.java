package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.Deposit;
import ru.mts.entity.DepositRate;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.Request;
import ru.mts.exception.ValidationException;
import ru.mts.repository.DepositRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class DepositServiceImpl {
    private final DepositRepository depositRepository;
    private final RequestServiceImpl requestService;
    private final UtilityServiceImpl utilityService;

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository, RequestServiceImpl requestService, UtilityServiceImpl utilityService) {
        this.depositRepository = depositRepository;
        this.requestService = requestService;
        this.utilityService = utilityService;
    }

    //создает вклад по заявке idRequest
    public Deposit createDepositByIdRequest(Integer idCustomer, Integer idRequest, BigDecimal numBankAccounts) {
        Request request = requestService.getRequestById(idRequest);
        Deposit deposit = new Deposit();
        deposit.setRequest(request);
        deposit.setCustomerId(idCustomer);
        deposit.setDepositRefill(request.isDepositRefill());
        deposit.setReductionOfDeposit(request.isReductionOfDeposit());
        deposit.setDepositsType(requestService.getDepositsTypeByRequest(request.isDepositRefill(), request.isReductionOfDeposit()));
        deposit.setStartDate(request.getRequestDateTime());
        deposit.setDepositTerm(request.getDepositTerm());
        deposit.setEndDate(getEndDateForDeposit(request.getRequestDateTime(), request.getDepositTerm())); //написать метод на определение конца вклада - старт + срок + 1 день
        deposit.setDepositAmount(request.getDepositAmount());
        deposit.setDepositRate(getDepositRateByParam(request.isDepositRefill(), request.isReductionOfDeposit(), request.getDepositTerm(), request.getDepositAmount()));  //написать метод на определение ставки от: вид, срок, сумма
        deposit.setTypesPercentPayment(request.getTypesPercentPayment());
        deposit.setDepositAccountId(numBankAccounts); // счет депозита
        deposit.setDepositDebitingAccountId(request.getDepositDebitingAccountId()); //счет для списания суммы депозита
        deposit.setPercentPaymentAccountId(request.getPercentPaymentAccountId()); //счет для выплаты процентов
        deposit.setDepositRefundAccountId(request.getDepositRefundAccountId()); //счет для возвращения вклада
        deposit.setActive(true); //вклад активный, отображать, снимать, пополнять - от условий

        return depositRepository.save(deposit);
    }

    //метод, который возвращает дто для отображения
    //вернуть дто для отображения в удачном сообщении, сделать депозит и оттуда взять ставку и вид вклада

    //метод для определения конца срока действия вклада
    public OffsetDateTime getEndDateForDeposit(OffsetDateTime requestDateTime, DepositTerm depositTerm) {
        List<DepositTerm> depositTerms = utilityService.getDepositTerms();
        OffsetDateTime endDateDeposit = null;
        if (depositTerm.getDepositTermName().equals("3 мес.")) {
            endDateDeposit = requestDateTime.plusMonths(3);
        }
        if (depositTerm.getDepositTermName().equals("6 мес.")) {
            endDateDeposit = requestDateTime.plusMonths(6);
        }
        if (depositTerm.getDepositTermName().equals("1 год.")) {
            endDateDeposit = requestDateTime.plusYears(1);
        }
        return endDateDeposit;
    }

    //метод для определения процентной ставки от параметров: вид вклада, срок, сумма
    public DepositRate getDepositRateByParam(boolean isDepositRefill, boolean isReductionOfDeposit, DepositTerm depositTerm, BigDecimal depositAmount) {
        BigDecimal sum50 = new BigDecimal("50000");
        List<DepositRate> depositRates = utilityService.getDepositRates();
        DepositRate depositRate = null;
        //на "1 год.", разделение по сумме 50 000 руб
        if (depositTerm.getDepositTermName().equals("1 год.")) {
            if (depositAmount.compareTo(sum50) >= 0) {
                if (isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(1);
                }
                if (isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(2);
                }
                if (!isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(3);
                }
                if (!isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(4);
                }
            } else {
                if (isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(13);
                }
                if (isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(14);
                }
                if (!isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(15);
                }
                if (!isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(16);
                }
            }
        }
        //на "6 мес.", разделение по сумме 50 000 руб
        if (depositTerm.getDepositTermName().equals("6 мес.")) {
            if (depositAmount.compareTo(sum50) >= 0) {
                if (isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(5);
                }
                if (isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(6);
                }
                if (!isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(7);
                }
                if (!isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(8);
                }
            } else {
                if (isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(17);
                }
                if (isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(18);
                }
                if (!isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(19);
                }
                if (!isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(20);
                }
            }
        }

        //на "3 мес.", разделение по сумме 50 000 руб
        if (depositTerm.getDepositTermName().equals("3 мес.")) {
            if (depositAmount.compareTo(sum50) >= 0) {
                if (isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(9);
                }
                if (isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(10);
                }
                if (!isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(11);
                }
                if (!isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(12);
                }
            } else {
                if (isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(21);
                }
                if (isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(22);
                }
                if (!isDepositRefill && !isReductionOfDeposit) {
                    depositRate = depositRates.get(23);
                }
                if (!isDepositRefill && isReductionOfDeposit) {
                    depositRate = depositRates.get(24);
                }
            }
        }
        return depositRate;
    }


    //метод определения даты для выплаты процентов - разные варианты помесячно, в конце срока


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