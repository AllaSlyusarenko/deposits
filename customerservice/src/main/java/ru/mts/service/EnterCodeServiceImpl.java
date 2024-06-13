package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.EnterCode;
import ru.mts.exception.ValidationException;
import ru.mts.repository.EnterCodeRepository;

import java.time.OffsetDateTime;
import java.util.Random;

@Service
public class EnterCodeServiceImpl {
    private final EnterCodeRepository enterCodeRepository;

    @Autowired
    public EnterCodeServiceImpl(EnterCodeRepository enterCodeRepository) {
        this.enterCodeRepository = enterCodeRepository;
    }

    //получить последний код по customerId
    public String getLastEnterCodeByIdCustomer(Integer customerId) {
        checkId(customerId);
        EnterCode enterCode = enterCodeRepository.findFirstByIdCustomerOrderByIdEnterCodeDesc(customerId);
        return enterCode.getCode();
    }
    //получить время последнего кода по customerId
    public OffsetDateTime getLastEnterCodeDateTimeByIdCustomer(Integer customerId) {
        checkId(customerId);
        EnterCode enterCode = enterCodeRepository.findFirstByIdCustomerOrderByIdEnterCodeDesc(customerId);
        return enterCode.getCodeDateTime();
    }
    //сохранить/ отправить код по customerId
    public EnterCode saveEnterCode(Integer customerId) {
        EnterCode enterCodeIn = new EnterCode();
        int code = createCode();
        enterCodeIn.setCode(String.valueOf(code));
        enterCodeIn.setIdCustomer(customerId);
        return enterCodeRepository.save(enterCodeIn);
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