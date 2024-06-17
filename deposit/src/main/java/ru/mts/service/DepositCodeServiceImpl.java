package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.annotation.Logging;
import ru.mts.entity.DepositCode;
import ru.mts.exception.ValidationException;
import ru.mts.repository.DepositCodeRepository;

import java.time.OffsetDateTime;
import java.util.Random;

@Service
public class DepositCodeServiceImpl {
    private final DepositCodeRepository depositCodeRepository;

    @Autowired
    public DepositCodeServiceImpl(DepositCodeRepository depositCodeRepository) {
        this.depositCodeRepository = depositCodeRepository;
    }

    /**
     * Метод - получить последний код по idDeposit
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public String getLastDepositCodeByIdDeposit(Integer idDeposit) {
        checkId(idDeposit);
        DepositCode depositCode = depositCodeRepository.findFirstByIdDepositOrderByIdDepositCodeDesc(idDeposit);
        return depositCode.getCode();
    }

    /**
     * Метод - получить время последнего кода по idDeposit
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public OffsetDateTime getLastDepositCodeDateTimeByIdDepositCode(Integer idDeposit) {
        checkId(idDeposit);
        DepositCode depositCode = depositCodeRepository.findFirstByIdDepositOrderByIdDepositCodeDesc(idDeposit);
        return depositCode.getCodeDateTime();
    }

    /**
     * Метод - сохранить и отправить код по idDeposit
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public DepositCode saveDepositCode(Integer idDeposit) {
        DepositCode depositCode = new DepositCode();
        int code = createCode();
        depositCode.setCode(String.valueOf(code));
        depositCode.setIdDeposit(idDeposit);
        return depositCodeRepository.save(depositCode);
    }

    /**
     * Метод - проверка id
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }

    /**
     * Метод - сгенерить 4-значный код
     */
    @Logging(entering = true, exiting = true)
    private int createCode() {
        int maximum = 9999;
        int minimum = 1000;
        Random rn = new Random();
        int randomNum = rn.nextInt(maximum - minimum + 1) + minimum;
        return randomNum;
    }
}