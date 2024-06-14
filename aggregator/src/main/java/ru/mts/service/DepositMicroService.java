package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.entity.*;
import ru.mts.exception.UnexpectedException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DepositMicroService {
    private final RestTemplate restTemplate;
    private final CustomerMicroService customerMicroService;

    @Autowired
    public DepositMicroService(RestTemplate restTemplate, CustomerMicroService customerMicroService) {
        this.restTemplate = restTemplate;
        this.customerMicroService = customerMicroService;
    }

    //получить все depositTerm
    public List<DepositTerm> getAllDepositTerm() {
        ResponseEntity<List<DepositTerm>> depositTerms =
                restTemplate.exchange("http://localhost:8082/deposit/alldepositterm",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<DepositTerm>>() {
                        }
                );
        if (depositTerms.getStatusCode().is2xxSuccessful()) {
            return depositTerms.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом deposit " + depositTerms.getBody());
        }
    }

    //получить все TypesPercentPayment
    public List<TypesPercentPayment> getAllTypesPercentPayment() {
        ResponseEntity<List<TypesPercentPayment>> typesPercentPayments =
                restTemplate.exchange("http://localhost:8082/deposit/alltypespercent",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<TypesPercentPayment>>() {
                        }
                );
        if (typesPercentPayments.getStatusCode().is2xxSuccessful()) {
            return typesPercentPayments.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом deposit " + typesPercentPayments.getBody());
        }
    }

    public boolean checkRequestIn(RequestIn requestin) {
        boolean b = false;
        if (!requestin.getIsDepositRefill().isBlank() && (requestin.getIsDepositRefill().equals("да")
                || requestin.getIsDepositRefill().equals("нет"))) {
            b = true;
        } else {
            return false;
        }
        if (!requestin.getIsReductionOfDeposit().isBlank() && (requestin.getIsReductionOfDeposit().equals("да")
                || requestin.getIsReductionOfDeposit().equals("нет"))) {
            b = true;
        } else {
            return false;
        }
        if (!requestin.getDepositTerm().getDepositTermName().isBlank()) {
            b = true;
        } else {
            return false;
        }
        if (!requestin.getDepositAmount().toString().isBlank() && requestin.getDepositAmount().subtract(new BigDecimal(10000)).compareTo(BigDecimal.ZERO) >= 0) {
            b = true;
        } else {
            return false;
        }
        if (!requestin.getTypesPercentPayment().getTypePercentPaymentPeriod().isBlank()) {
            b = true;
        } else {
            return false;
        }
        if (!requestin.getDepositDebitingAccountId().toString().isBlank()) {
            b = true;
        } else {
            return false;
        }
        if (!requestin.getPercentPaymentAccountId().toString().isBlank()) {
            b = true;
        } else {
            return false;
        }

        if (!requestin.getDepositRefundAccountId().toString().isBlank()) {
            b = true;
        } else {
            return false;
        }
        return b;
    }

    //отправить заявку на сохранение
    public Integer saveRequest(Request request) {
//        Integer idCustomer = customerMicroService.getCustomerIdByPhoneNumber();
        ResponseEntity<Integer> idRequest =
                restTemplate.postForEntity("http://localhost:8082/request/" + CustomerMicroService.getIdCustomer() + "/save",
                        request,
                        Integer.class);
        if (idRequest.getStatusCode().is2xxSuccessful()) {
            return idRequest.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом deposit " + idRequest.getBody());
        }
    }

    //отправить смс для подтверждения
    public void sendRequestCode(Integer idRequest) {
        String url = "http://localhost:8082/request/sendcode/" + idRequest + "/" + CustomerMicroService.getPhoneNumber();
        ResponseEntity<Boolean> code = restTemplate.getForEntity(url, Boolean.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом deposit " + code.getBody());
        }
    }

    //проверка правильности смс кода для Request
    public Boolean checkRequestCode(String code) {
//        Integer idCustomer = customerMicroService.getCustomerIdByPhoneNumber();
        ResponseEntity<Boolean> isOk =
                restTemplate.getForEntity("http://localhost:8082/request/checkcode/" + code + "/" + CustomerMicroService.getIdCustomer(), Boolean.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + code);
        }
    }

    //получить данные из request для проверки в account на наличие суммы на счету
    public RequestData getRequestData() {
//        Integer idCustomer = customerMicroService.getCustomerIdByPhoneNumber();
        ResponseEntity<RequestData> data =
                restTemplate.getForEntity("http://localhost:8082/request/requestdata/" + CustomerMicroService.getIdCustomer(), RequestData.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + data);
        }
    }

    //присвоить заявке статус - Одобрена
    public Boolean changeStatusOk() {
//        Integer idCustomer = customerMicroService.getCustomerIdByPhoneNumber();
        ResponseEntity<Boolean> data =
                restTemplate.getForEntity("http://localhost:8082/request/changestatusok/" + CustomerMicroService.getIdCustomer(), Boolean.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + data);
        }
    }

    //присвоить заявке статус - Отклонена
    public Boolean changeStatusNotOk() {
        ResponseEntity<Boolean> data =
                restTemplate.getForEntity("http://localhost:8082/request/changestatusnotok/" + CustomerMicroService.getIdCustomer(), Boolean.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + data);
        }
    }

    //запрос на создание вклада из заявки idRequest по idCustomer
    public DepositSuccess createDepositByIdRequest(Integer idRequest, String numBankAccounts) {
        String url = "http://localhost:8082/deposit/createdepositbyidrequest/"
                + CustomerMicroService.getIdCustomer() + "/" + idRequest + "/" + numBankAccounts;
        ResponseEntity<DepositSuccess> data = restTemplate.getForEntity(url, DepositSuccess.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + data);
        }
    }

    //для отображения краткой информации по вкладам по idCustomer
    public List<DepositShort> getAllDepositShortActiveDeposits() {
        String url = "http://localhost:8082/deposit/allshortdepositsactive/" + CustomerMicroService.getIdCustomer();
        ResponseEntity<List<DepositShort>> dtos = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DepositShort>>() {
                }
        );
        if (dtos.getStatusCode().is2xxSuccessful()) {
            return dtos.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + dtos);
        }
    }

    //для отображения полной информации по id вклада
    public DepositFull showFullDeposit(Integer idDeposit) {
        String url = "http://localhost:8082/deposit/showfulldeposit/" + idDeposit;
        ResponseEntity<DepositFull> dtoFull = restTemplate.getForEntity(url, DepositFull.class);
        if (dtoFull.getStatusCode().is2xxSuccessful()) {
            return dtoFull.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + dtoFull);
        }
    }

    //код для подтверждения закрытия вклада по id вклада
    public void sendDepositCodeClose(Integer idDeposit) {
        String url = "http://localhost:8082/deposit/codeclosedeposit/" + idDeposit;
        ResponseEntity<String> code = restTemplate.getForEntity(url, String.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + idDeposit);
        }
    }


    //получить все отклоненные заявки по customerId
    public List<RequestNotOk> getRequestNotOk() {
        String url = "http://localhost:8082/request/requestnotok/" + CustomerMicroService.getIdCustomer();
        ResponseEntity<List<RequestNotOk>> data =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<RequestNotOk>>() {
                        }
                );
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + data);
        }
    }

    //удалить заявку по id - поменять её статус на 5 - Заявка удалена
    public void deleteRequest(Integer idRequest) {
        String url = "http://localhost:8082/request/deleterequest/" + idRequest;
        ResponseEntity<Boolean> code = restTemplate.getForEntity(url, Boolean.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом deposit " + code.getBody());
        }
    }

    //закрыть вклад по id - возвращаю dto с данными для сервиса account
    public CloseDeposit closeDeposit(Integer idDeposit) {
        String url = "http://localhost:8082/deposit/closedeposit/" + idDeposit;
        ResponseEntity<CloseDeposit> code = restTemplate.getForEntity(url, CloseDeposit.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            return code.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом deposit " + code.getBody());
        }
    }


}