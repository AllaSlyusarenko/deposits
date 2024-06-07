package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.EnterCode;
import ru.mts.exception.ValidationException;
import ru.mts.repository.EnterCodeRepository;

import java.time.OffsetDateTime;

@Service
public class EnterCodeServiceImpl {
    private final EnterCodeRepository enterCodeRepository;

    @Autowired
    public EnterCodeServiceImpl(EnterCodeRepository enterCodeRepository) {
        this.enterCodeRepository = enterCodeRepository;
    }

    public String getLastEnterCodeByIdCustomer(Integer customerId) {
        checkId(customerId);
        EnterCode enterCode = enterCodeRepository.findFirstByCustomer_IdCustomersOrderByIdEnterCodeDesc(customerId);
        return enterCode.getCode();
    }

    public OffsetDateTime getLastEnterCodeDateTimeByIdCustomer(Integer customerId) {
        checkId(customerId);
        EnterCode enterCode = enterCodeRepository.findFirstByCustomer_IdCustomersOrderByIdEnterCodeDesc(customerId);
        return enterCode.getCodeDateTime();
    }

    //проверка id
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }
}