package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.annotation.Logging;
import ru.mts.entity.EnterCode;
import ru.mts.exception.ValidationException;
import ru.mts.repository.EnterCodeRepository;

import java.time.OffsetDateTime;
import java.util.Random;

@Service
public class EnterCodeServiceImpl {
    private final EnterCodeRepository enterCodeRepository;
    private final int MAXIMUM = 9999;
    private final int MINIMUM = 1000;
    Random rn = new Random();

    @Autowired
    public EnterCodeServiceImpl(EnterCodeRepository enterCodeRepository) {
        this.enterCodeRepository = enterCodeRepository;
    }

    /**
     * Метод - получения последнего кода по customerId
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public String getLastEnterCodeByIdCustomer(Integer customerId) {
        checkId(customerId);
        EnterCode enterCode = enterCodeRepository.findFirstByIdCustomerOrderByIdEnterCodeDesc(customerId);
        return enterCode.getCode();
    }

    /**
     * Метод - получения времени последнего кода по customerId
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public OffsetDateTime getLastEnterCodeDateTimeByIdCustomer(Integer customerId) {
        checkId(customerId);
        EnterCode enterCode = enterCodeRepository.findFirstByIdCustomerOrderByIdEnterCodeDesc(customerId);
        return enterCode.getCodeDateTime();
    }

    /**
     * Метод - сохранить/ отправить код по customerId
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public EnterCode saveEnterCode(Integer customerId) {
        EnterCode enterCodeIn = new EnterCode();
        int code = createCode();
        enterCodeIn.setCode(String.valueOf(code));
        enterCodeIn.setIdCustomer(customerId);
        return enterCodeRepository.save(enterCodeIn);
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
    @Logging(entering = true, exiting = true, logArgs = true)
    private int createCode() {
        int randomNum = rn.nextInt(MAXIMUM - MINIMUM + 1) + MINIMUM;
        return randomNum;
    }
}