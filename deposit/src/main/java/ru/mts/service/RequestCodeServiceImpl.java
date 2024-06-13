package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    //получить последний код по idRequest
    public String getLastRequestCodeByIdRequest(Integer idRequest) {
        checkId(idRequest);
        RequestCode requestCode = requestCodeRepository.findFirstByIdRequestOrderByIdRequestCodeDesc(idRequest);
        return requestCode.getCode();
    }

    //получить время последнего кода по idRequest
    public OffsetDateTime getLastRequestCodeDateTimeByIdRequestCode(Integer idRequest) {
        checkId(idRequest);
        RequestCode requestCode = requestCodeRepository.findFirstByIdRequestOrderByIdRequestCodeDesc(idRequest);
        return requestCode.getCodeDateTime();
    }

    //сохранить и отправить код по idRequest
    public RequestCode saveRequestCode(Integer idRequest) {
        RequestCode requestCode = new RequestCode();
        int code = createCode();
        requestCode.setCode(String.valueOf(code));
        requestCode.setIdRequest(idRequest);
        return requestCodeRepository.save(requestCode);
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
