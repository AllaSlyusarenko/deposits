package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    //получить последний код по idDeposit
    public String getLastDepositCodeByIdDeposit(Integer idDeposit) {
        checkId(idDeposit);
        DepositCode depositCode = depositCodeRepository.findFirstByIdDepositOrderByIdDepositCodeDesc(idDeposit);
        return depositCode.getCode();
    }

    //получить время последнего кода по idDeposit
    public OffsetDateTime getLastDepositCodeDateTimeByIdDepositCode(Integer idDeposit) {
        checkId(idDeposit);
        DepositCode depositCode = depositCodeRepository.findFirstByIdDepositOrderByIdDepositCodeDesc(idDeposit);
        return depositCode.getCodeDateTime();
    }

    //сохранить и отправить код по idDeposit
    public DepositCode saveDepositCode(Integer idDeposit) {
        DepositCode depositCode = new DepositCode();
        int code = createCode();
        depositCode.setCode(String.valueOf(code));
        depositCode.setIdDeposit(idDeposit);
        return depositCodeRepository.save(depositCode);
    }

    //проверка id
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }

    //сгенерить 4-значный код
    private int createCode() {
        int maximum = 9999;
        int minimum = 1000;
        Random rn = new Random();
        int randomNum = rn.nextInt(maximum - minimum + 1) + minimum;
        return randomNum;
    }
}