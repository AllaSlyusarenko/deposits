package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.RequestCode;
import ru.mts.exception.ValidationException;
import ru.mts.repository.RequestCodeRepository;

import java.time.OffsetDateTime;

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
        int code = Math.toIntExact(Math.round(Math.random() * 9998));
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
}
