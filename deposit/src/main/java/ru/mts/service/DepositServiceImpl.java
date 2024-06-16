package ru.mts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.annotation.Logging;
import ru.mts.dto.*;
import ru.mts.entity.*;
import ru.mts.exception.ValidationException;
import ru.mts.mapper.DepositMapper;
import ru.mts.repository.CurrentDepositStatusRepository;
import ru.mts.repository.DepositRepository;
import ru.mts.repository.DepositStatusRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
public class DepositServiceImpl {
    private final DepositRepository depositRepository;
    private final RequestServiceImpl requestService;
    private final UtilityServiceImpl utilityService;
    private final DepositStatusRepository depositStatusRepository;
    private final CurrentDepositStatusRepository currentDepositStatusRepository;
    private final DepositCodeServiceImpl depositCodeServiceImpl;

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository, RequestServiceImpl requestService, UtilityServiceImpl utilityService, DepositStatusRepository depositStatusRepository, CurrentDepositStatusRepository currentDepositStatusRepository, DepositCodeServiceImpl depositCodeServiceImpl) {
        this.depositRepository = depositRepository;
        this.requestService = requestService;
        this.utilityService = utilityService;
        this.depositStatusRepository = depositStatusRepository;
        this.currentDepositStatusRepository = currentDepositStatusRepository;
        this.depositCodeServiceImpl = depositCodeServiceImpl;
    }

    /**
     * Метод - создает вклад по заявке idRequest и возвращает dto
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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
        Deposit depositSave = depositRepository.save(deposit);
        //статус - 1 - Вклад открыт
        statusDepositSet(depositSave, 1);
        return depositSave;
    }

    /**
     * Метод - возвращает дто для отображения при создании вклада
     */
    @Logging(entering = true, exiting = true)
    public DepositOutSuccessDto depositOutSuccess(Deposit deposit) {
        DepositOutSuccessDto depositOutSuccessDto = new DepositOutSuccessDto();
        depositOutSuccessDto.setIdRequest(deposit.getRequest().getIdRequest());
        depositOutSuccessDto.setDepositsType(deposit.getDepositsType().getDepositsTypesName());
        depositOutSuccessDto.setDepositAmount(deposit.getDepositAmount());
        depositOutSuccessDto.setStartDate(deposit.getStartDate());
        depositOutSuccessDto.setDepositRate(deposit.getDepositRate().getDepositRate());
        return depositOutSuccessDto;
    }

    /**
     * Метод - для определения конца срока действия вклада
     */
    @Logging(entering = true, exiting = true)
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

    /**
     * Метод - для определения процентной ставки от параметров: вид вклада, срок, сумма
     */
    @Logging(entering = true, exiting = true)
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

    /**
     * Метод - присвоить статус: 1 - Вклад открыт, 8 - Подтверждение закрытия вклада, 9 - Закрытие вклада подтверждено,
     * 10 - Вклад закрыт(isActive = false)
     */
    @Logging(entering = true, exiting = true)
    public void statusDepositSet(Deposit deposit, int idStatusDeposit) {
        CurrentDepositStatus currentDepositStatus = new CurrentDepositStatus();
        currentDepositStatus.setIdDeposit(deposit);
        DepositStatus depositStatus = depositStatusRepository.findById(idStatusDeposit);
        currentDepositStatus.setIdDepositStatus(depositStatus);
        currentDepositStatusRepository.save(currentDepositStatus);
    }

    /**
     * Метод - получить все активные депозиты по idCustomer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public List<Deposit> getAllByIdCustomerActiveDeposits(Integer idCustomer) {
        List<Deposit> deposits = depositRepository.findAllByCustomerIdAndIsActiveOrderByDepositAmountDesc(idCustomer, true);
        return deposits;
    }

    /**
     * Метод - для отображения краткой информации DepositOutShortDto из метода все активные депозиты по idCustomer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public List<DepositOutShortDto> getAllDepositOutShortDtoActiveDeposits(Integer idCustomer) {
        List<Deposit> deposits = getAllByIdCustomerActiveDeposits(idCustomer);
        return DepositMapper.toDepositOutShortDtos(deposits);
    }

    /**
     * Метод - для отображения полной информации по id вклада
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public DepositOutFullDto showFullDeposit(Integer idDeposit) {
        Deposit deposit = depositRepository.findByIdDeposit(idDeposit);
        return DepositMapper.toDepositOutFullDto(deposit);
    }

    /**
     * Метод - для закрытия вклада по id, возвращает счет вклада BigDecimal depositAccountId, сумму вклада,
     * счет для возвращения суммы вклада
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public CloseDepositDto closeDeposit(Integer idDeposit) {
        Deposit deposit = depositRepository.findByIdDeposit(idDeposit);
        //вклад становится неактивным
        deposit.setActive(false);
        //статус вклада - 10 - Вклад закрыт
        statusDepositSet(deposit, 10);
        CloseDepositDto closeDepositDto = new CloseDepositDto();
        closeDepositDto.setDepositAccountId(deposit.getDepositAccountId());
        closeDepositDto.setDepositRefundAccountId(deposit.getDepositRefundAccountId());
        closeDepositDto.setDepositAmount(deposit.getDepositAmount());
        return closeDepositDto;
    }

    /**
     * Метод - отправить код для подтверждения закрытия вклада
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public String sendDepositCodeClose(Integer idDeposit, String phoneNumber) {
        Deposit deposit = depositRepository.findByIdDeposit(idDeposit);
        DepositCode depositCode = depositCodeServiceImpl.saveDepositCode(idDeposit);
        String message = "пользователю на номер " + phoneNumber + " отправлено смс с кодом";
        log.info(message);
        log.info(depositCode.getCode());
        //статус - подтверждение закрытия
        CurrentDepositStatus statusIn = new CurrentDepositStatus();
        statusIn.setIdDeposit(deposit);
        DepositStatus depositStatus = depositStatusRepository.findById(8);
        statusIn.setIdDepositStatus(depositStatus);
        currentDepositStatusRepository.save(statusIn);
        return message;
    }

    /**
     * Метод - проверка кода для подтверждения закрытия вклада
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public boolean checkCodeCloseDeposit(Integer idDeposit, String phoneNumber, String code) {
        Deposit deposit = depositRepository.findByIdDeposit(idDeposit);
        Integer depositId = deposit.getIdDeposit();
        DepositCloseCodeIn depositCodeIn = new DepositCloseCodeIn();
        depositCodeIn.setCode(code);
        depositCodeIn.setCodeDateTime(OffsetDateTime.now());
        depositCodeIn.setIdDeposit(depositId);

        String lastCode = depositCodeServiceImpl.getLastDepositCodeByIdDeposit(idDeposit);
        OffsetDateTime lastDateTime = depositCodeServiceImpl.getLastDepositCodeDateTimeByIdDepositCode(depositId);
        if (lastCode.equals(depositCodeIn.getCode()) &&
                depositCodeIn.getCodeDateTime().isAfter(lastDateTime) && depositCodeIn.getCodeDateTime().isBefore(lastDateTime.plusMinutes(1))) {
            //присвоить статус - Заявка подтверждена
            CurrentDepositStatus statusIn = new CurrentDepositStatus();
            statusIn.setIdDeposit(deposit);
            DepositStatus depositStatus = depositStatusRepository.findById(9);
            statusIn.setIdDepositStatus(depositStatus);
            currentDepositStatusRepository.save(statusIn);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод - проверка id
     */
    @Logging(entering = true, exiting = true)
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }
}