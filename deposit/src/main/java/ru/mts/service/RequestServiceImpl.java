package ru.mts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.dto.RequestDtoIn;
import ru.mts.entity.*;
import ru.mts.exception.ValidationException;
import ru.mts.repository.*;

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
    public Request createRequest(Integer customerId, RequestDtoIn requestDtoIn) {
        Request request = new Request();
        request.setCustomerId(customerId);
        request.setRequestDateTime(requestDtoIn.getRequestDateTime());
        request.setDepositRefill(requestDtoIn.isDepositRefill());
        request.setReductionOfDeposit(requestDtoIn.isReductionOfDeposit());
        request.setDepositAmount(requestDtoIn.getDepositAmount());
        request.setPercentPaymentAccountId(requestDtoIn.getPercentPaymentAccountId());
        request.setDepositRefundAccountId(requestDtoIn.getDepositRefundAccountId());
        request.setDepositDebitingAccountId(requestDtoIn.getDepositDebitingAccountId());

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
        CurrentRequestStatus status = currentRequestStatusRepository.save(statusIn);
        //отправить код по телефону - customerId
        return createdRequest;
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

    //проверка номера телефона
    private boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank() || phoneNumber.length() != 11) {
            throw new ValidationException("Неверный номер телефона " + phoneNumber);
        }
        return true;
    }
}