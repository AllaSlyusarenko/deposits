package ru.mts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.dto.RequestCodeIn;
import ru.mts.dto.RequestInDto;
import ru.mts.entity.*;
import ru.mts.exception.NotFoundException;
import ru.mts.exception.ValidationException;
import ru.mts.repository.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Slf4j
@Service
public class RequestServiceImpl {
    private final RequestRepository requestRepository;
    private final DepositTermRepository depositTermRepository;
    private final TypesPercentPaymentRepository typesPercentPaymentRepository;
    private final CurrentRequestStatusRepository currentRequestStatusRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final RequestCodeServiceImpl requestCodeService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, DepositTermRepository depositTermRepository, TypesPercentPaymentRepository typesPercentPaymentRepository, CurrentRequestStatusRepository currentRequestStatusRepository, RequestStatusRepository requestStatusRepository, RequestCodeServiceImpl requestCodeService) {
        this.requestRepository = requestRepository;
        this.depositTermRepository = depositTermRepository;
        this.typesPercentPaymentRepository = typesPercentPaymentRepository;
        this.currentRequestStatusRepository = currentRequestStatusRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.requestCodeService = requestCodeService;
    }


    //создать заявку
    public Integer createRequest(Integer customerId, RequestInDto requestDtoIn) {
        //найти все составляющие id
        Request request = new Request();
        request.setCustomerId(customerId);
//        request.setRequestDateTime(OffsetDateTime.now());
        request.setDepositRefill(requestDtoIn.isDepositRefill());
        request.setReductionOfDeposit(requestDtoIn.isReductionOfDeposit());
        request.setDepositAmount(requestDtoIn.getDepositAmount());
        request.setPercentPaymentAccountId(new BigDecimal(requestDtoIn.getPercentPaymentAccountId()));
        request.setDepositRefundAccountId(new BigDecimal(requestDtoIn.getDepositRefundAccountId()));
        request.setDepositDebitingAccountId(new BigDecimal(requestDtoIn.getDepositDebitingAccountId()));

        //DepositTerm
        DepositTerm depositTerm = depositTermRepository.findDepositTermByDepositTermName(requestDtoIn.getDepositTerm());
        request.setDepositTerm(depositTerm);
        //TypesPercentPayment
        TypesPercentPayment typesPercentPayment =
                typesPercentPaymentRepository.findTypesPercentPaymentByTypePercentPaymentPeriod(requestDtoIn.getTypesPercentPayment());
        request.setTypesPercentPayment(typesPercentPayment);
        Request createdRequest = requestRepository.save(request);

        //присвоить статус - Подтверждение заявки
        CurrentRequestStatus statusIn = new CurrentRequestStatus();
        statusIn.setIdRequest(createdRequest);
        RequestStatus requestStatus = requestStatusRepository.findById(1);
        statusIn.setIdRequestStatus(requestStatus);
        currentRequestStatusRepository.save(statusIn);
        //отправить код по телефону - customerId
        return createdRequest.getIdRequest();
    }

    //отправить смс с кодом
    public String sendRequestCode(Integer idRequest, String phoneNumber) {
        checkPhoneNumber(phoneNumber);
        RequestCode requestCode = requestCodeService.saveRequestCode(idRequest);
        String message = "Пользователю на номер " + phoneNumber + " отправлено смс с кодом";
        log.info(message);
        log.info(requestCode.getCode());
        return message;
    }

    public boolean checkEnterCode(Integer requestId, RequestCodeIn requestCodeIn) {
//        Integer id = enterCodeIn.getIdCustomer();
//        checkId(id);
        String lastCode = requestCodeService.getLastRequestCodeByIdRequestCode(requestId);
        OffsetDateTime lastDateTime = requestCodeService.getLastRequestCodeDateTimeByIdRequestCode(requestId);
        return lastCode.equals(requestCodeIn.getCode()) &&
                requestCodeIn.getCodeDateTime().isAfter(lastDateTime) && requestCodeIn.getCodeDateTime().isBefore(lastDateTime.plusMinutes(1));
    }

    public boolean checkRequestCode(Integer customerId, String code) {
//        Integer id = enterCodeIn.getIdCustomer();
//        checkId(id);
        //по customerId найти id последней заявки
        Request request = requestRepository.findFirstByCustomerIdOrderByIdRequestDesc(customerId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        Integer requestId = request.getIdRequest();
        RequestCodeIn requestCodeIn = new RequestCodeIn();
        requestCodeIn.setCode(code);
        requestCodeIn.setCodeDateTime(OffsetDateTime.now());
        requestCodeIn.setIdRequest(requestId);

        String lastCode = requestCodeService.getLastRequestCodeByIdRequest(requestId);
        OffsetDateTime lastDateTime = requestCodeService.getLastRequestCodeDateTimeByIdRequestCode(requestId);
        if (lastCode.equals(requestCodeIn.getCode()) &&
                requestCodeIn.getCodeDateTime().isAfter(lastDateTime) && requestCodeIn.getCodeDateTime().isBefore(lastDateTime.plusMinutes(1))) {
            //присвоить статус - Заявка подтверждена
            CurrentRequestStatus statusIn = new CurrentRequestStatus();
            statusIn.setIdRequest(request);
            RequestStatus requestStatus = requestStatusRepository.findById(2);
            statusIn.setIdRequestStatus(requestStatus);
            currentRequestStatusRepository.save(statusIn);
            return true;
        } else {
            return false;
        }
    }


    //проверка id
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }

    //проверка номера телефона
    private boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank() || phoneNumber.length() != 11) {
            throw new ValidationException("Неверный номер телефона " + phoneNumber);
        }
        return true;
    }
}