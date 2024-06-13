package ru.mts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.mts.dto.RequestCodeIn;
import ru.mts.dto.RequestDataOut;
import ru.mts.dto.RequestInDto;
import ru.mts.dto.RequestNotOkDto;
import ru.mts.entity.*;
import ru.mts.exception.NotFoundException;
import ru.mts.exception.ValidationException;
import ru.mts.mapper.RequestMapper;
import ru.mts.repository.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestServiceImpl {
    private final RequestRepository requestRepository;
    private final DepositTermRepository depositTermRepository;
    private final TypesPercentPaymentRepository typesPercentPaymentRepository;
    private final CurrentRequestStatusRepository currentRequestStatusRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final RequestCodeServiceImpl requestCodeService;
    private final UtilityServiceImpl utilityService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, DepositTermRepository depositTermRepository, TypesPercentPaymentRepository typesPercentPaymentRepository, CurrentRequestStatusRepository currentRequestStatusRepository, RequestStatusRepository requestStatusRepository, RequestCodeServiceImpl requestCodeService, UtilityServiceImpl utilityService) {
        this.requestRepository = requestRepository;
        this.depositTermRepository = depositTermRepository;
        this.typesPercentPaymentRepository = typesPercentPaymentRepository;
        this.currentRequestStatusRepository = currentRequestStatusRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.requestCodeService = requestCodeService;
        this.utilityService = utilityService;
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

        return createdRequest.getIdRequest();
    }

    //отправить смс с кодом
    public String sendRequestCode(Integer idRequest, String phoneNumber) {
        checkPhoneNumber(phoneNumber);
        RequestCode requestCode = requestCodeService.saveRequestCode(idRequest);
        String message = "пользователю на номер " + phoneNumber + " отправлено смс с кодом";
        log.info(message);
        log.info(requestCode.getCode());
        return message;
    }

    public boolean checkEnterCode(Integer requestId, RequestCodeIn requestCodeIn) {
//        Integer id = enterCodeIn.getIdCustomer();
//        checkId(id);
        String lastCode = requestCodeService.getLastRequestCodeByIdRequest(requestId);
        OffsetDateTime lastDateTime = requestCodeService.getLastRequestCodeDateTimeByIdRequestCode(requestId);
        return lastCode.equals(requestCodeIn.getCode()) &&
                requestCodeIn.getCodeDateTime().isAfter(lastDateTime) && requestCodeIn.getCodeDateTime().isBefore(lastDateTime.plusMinutes(1));
    }

    //проверка смскода для подтверждения заявки
    public boolean checkRequestCode(Integer customerId, String code) {
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

    //получение данных из заявки для проверки достаточности суммы в account
    public RequestDataOut getRequestData(Integer customerId) {
        Request request = requestRepository.findFirstByCustomerIdOrderByIdRequestDesc(customerId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        RequestDataOut requestDataOut = new RequestDataOut();
        requestDataOut.setId(request.getIdRequest());
        requestDataOut.setDepositAmount(request.getDepositAmount());
        requestDataOut.setDepositDebitingAccountId(request.getDepositRefundAccountId());
        return requestDataOut;
    }

    //присвоить заявке статус - Одобрена
    public Boolean changeStatusOk(Integer customerId) {
        Request request = requestRepository.findFirstByCustomerIdOrderByIdRequestDesc(customerId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        CurrentRequestStatus statusIn = new CurrentRequestStatus();
        statusIn.setIdRequest(request);
        RequestStatus requestStatus = requestStatusRepository.findById(3);
        statusIn.setIdRequestStatus(requestStatus);
        currentRequestStatusRepository.save(statusIn);
        return true;
    }

    //присвоить заявке статус - Отклонена
    public Boolean changeStatusNotOk(Integer customerId) {
        Request request = requestRepository.findFirstByCustomerIdOrderByIdRequestDesc(customerId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        CurrentRequestStatus statusIn = new CurrentRequestStatus();
        statusIn.setIdRequest(request);
        RequestStatus requestStatus = requestStatusRepository.findById(4);
        statusIn.setIdRequestStatus(requestStatus);
        currentRequestStatusRepository.save(statusIn);
        return true;
    }

    //найти request по id
    public Request getRequestById(Integer requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Заявка не найдена"));
    }

    //метод на определение DepositsType вида вклада - входные из заявки
    public DepositsType getDepositsTypeByRequest(boolean isDepositRefill, boolean isReductionOfDeposit) {
        List<DepositsType> depositsTypes = utilityService.getDepositsTypes();
        DepositsType depositsType = null;
        if (isDepositRefill && isReductionOfDeposit) {
            depositsType = depositsTypes.get(1);
        }
        if (isDepositRefill && !isReductionOfDeposit) {
            depositsType = depositsTypes.get(2);
        }
        if (!isDepositRefill && !isReductionOfDeposit) {
            depositsType = depositsTypes.get(3);
        }
        if (!isDepositRefill && isReductionOfDeposit) {
            depositsType = depositsTypes.get(4);
        }
        return depositsType;
    }

    //найти все заявки пользователя со статусом отклонена с сортировкой по убыванию даты
    public List<RequestNotOkDto> requestnotok(Integer customerId) {
        List<Request> requestsNotOk = new ArrayList<>();
        List<Request> requests = requestRepository.findAllByCustomerIdOrderByRequestDateTimeDesc(customerId);
        for (Request request : requests) {
            List<CurrentRequestStatus> statuses = currentRequestStatusRepository.findAllByIdRequest(request);
            List<Integer> idsStatus = statuses.stream().map(c -> c.getIdRequestStatus().getIdRequestStatus()).collect(Collectors.toList());
            if (idsStatus.contains(4)) {
                requestsNotOk.add(request);
            }
        }
        return RequestMapper.toRequestNotOkDtos(requestsNotOk);
    }


    //удалить заявку по id
    public Boolean deleteRequest(Integer idRequest){
        return requestRepository.deleteByIdRequest(idRequest);
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