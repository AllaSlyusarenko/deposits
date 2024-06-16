package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.annotation.Logging;
import ru.mts.entity.RequestCode;
import ru.mts.exception.ValidationException;
import ru.mts.repository.RequestCodeRepository;

import java.time.OffsetDateTime;
import java.util.Random;

@Service
public class RequestCodeServiceImpl {
    private final RequestCodeRepository requestCodeRepository;

    @Autowired
    public RequestCodeServiceImpl(RequestCodeRepository requestCodeRepository) {
        this.requestCodeRepository = requestCodeRepository;
    }

    /**
     * Метод - получить последний код по idRequest
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public String getLastRequestCodeByIdRequest(Integer idRequest) {
        checkId(idRequest);
        RequestCode requestCode = requestCodeRepository.findFirstByIdRequestOrderByIdRequestCodeDesc(idRequest);
        return requestCode.getCode();
    }

    /**
     * Метод - получить время последнего кода по idRequest
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public OffsetDateTime getLastRequestCodeDateTimeByIdRequestCode(Integer idRequest) {
        checkId(idRequest);
        RequestCode requestCode = requestCodeRepository.findFirstByIdRequestOrderByIdRequestCodeDesc(idRequest);
        return requestCode.getCodeDateTime();
    }

    /**
     * Метод - сохранить и отправить код по idRequest
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public RequestCode saveRequestCode(Integer idRequest) {
        RequestCode requestCode = new RequestCode();
        int code = createCode();
        requestCode.setCode(String.valueOf(code));
        requestCode.setIdRequest(idRequest);
        return requestCodeRepository.save(requestCode);
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
        int maximum = 9999;
        int minimum = 1000;
        Random rn = new Random();
        int randomNum = rn.nextInt(maximum - minimum + 1) + minimum;
        return randomNum;
    }
}
