package ru.mts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.mts.annotation.Logging;
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

    /**
     * Метод - для создания заявки на вклад
     */
    @Logging(entering = true, exiting = true)
    public Integer createRequest(Integer customerId, RequestInDto requestDtoIn) {
        //найти все составляющие id
        Request request = new Request();
        request.setCustomerId(customerId);
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

    /**
     * Метод - отправить смс с кодом заявки на вклад
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public String sendRequestCode(Integer idRequest, String phoneNumber) {
        checkPhoneNumber(phoneNumber);
        RequestCode requestCode = requestCodeService.saveRequestCode(idRequest);
        String message = "пользователю на номер " + phoneNumber + " отправлено смс с кодом";
        log.info(message);
        log.info(requestCode.getCode());
        return message;
    }

    public boolean checkEnterCode(Integer requestId, RequestCodeIn requestCodeIn) {
        String lastCode = requestCodeService.getLastRequestCodeByIdRequest(requestId);
        OffsetDateTime lastDateTime = requestCodeService.getLastRequestCodeDateTimeByIdRequestCode(requestId);
        return lastCode.equals(requestCodeIn.getCode()) &&
                requestCodeIn.getCodeDateTime().isAfter(lastDateTime) && requestCodeIn.getCodeDateTime().isBefore(lastDateTime.plusMinutes(1));
    }

    /**
     * Метод - проверка смскода для подтверждения заявки
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - получение данных из заявки для проверки достаточности суммы в account
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public RequestDataOut getRequestData(Integer customerId) {
        Request request = requestRepository.findFirstByCustomerIdOrderByIdRequestDesc(customerId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        RequestDataOut requestDataOut = new RequestDataOut();
        requestDataOut.setId(request.getIdRequest());
        requestDataOut.setDepositAmount(request.getDepositAmount());
        requestDataOut.setDepositDebitingAccountId(request.getDepositRefundAccountId());
        return requestDataOut;
    }

    /**
     * Метод - присвоить заявке статус - Одобрена
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - присвоить заявке статус - Отклонена
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - найти request по id
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Request getRequestById(Integer requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Заявка не найдена"));
    }

    /**
     * Метод - для определения DepositsType вида вклада - входные из заявки
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - получить все заявки пользователя со статусом отклонена с сортировкой по убыванию даты
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public List<RequestNotOkDto> requestnotok(Integer customerId) {
        List<Request> requestsNotOk = new ArrayList<>();
        List<Request> requests = requestRepository.findAllByCustomerIdOrderByRequestDateTimeDesc(customerId);
        for (Request request : requests) {
            List<CurrentRequestStatus> statuses = currentRequestStatusRepository.findAllByIdRequest(request);
            List<Integer> idsStatus = statuses.stream().map(c -> c.getIdRequestStatus().getIdRequestStatus()).collect(Collectors.toList());
            if (idsStatus.contains(4) && !idsStatus.contains(5)) {
                requestsNotOk.add(request);
            }
        }
        return RequestMapper.toRequestNotOkDtos(requestsNotOk);
    }

    /**
     * Метод - удалить заявку по id, т.е. присвоить статус 5 - Заявка удалена
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Boolean deleteRequest(Integer idRequest) {
        Request request = requestRepository.findById(idRequest).orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        CurrentRequestStatus statusIn = new CurrentRequestStatus();
        statusIn.setIdRequest(request);
        RequestStatus requestStatus = requestStatusRepository.findById(5);
        statusIn.setIdRequestStatus(requestStatus);
        currentRequestStatusRepository.save(statusIn);
        return true;
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
     * Метод - проверка номера телефона
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    private boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank() || phoneNumber.length() != 11) {
            throw new ValidationException("Неверный номер телефона " + phoneNumber);
        }
        return true;
    }
}