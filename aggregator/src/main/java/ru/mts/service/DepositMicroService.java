package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.annotation.Logging;
import ru.mts.entity.*;
import ru.mts.exception.UnexpectedException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DepositMicroService {
    private final RestTemplate restTemplate;

    @Autowired
    public DepositMicroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Метод - получить все depositTerm
     */
    @Logging(entering = true, exiting = true)
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
            throw new UnexpectedException("Service Deposit, Ошибка при взаимодействии с сервисом deposit " + depositTerms.getBody());
        }
    }

    /**
     * Метод - получить все TypesPercentPayment
     */
    @Logging(entering = true, exiting = true)
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
            throw new UnexpectedException("Service Deposit, Ошибка при взаимодействии с сервисом deposit " + typesPercentPayments.getBody());
        }
    }

    /**
     * Метод - проверка заполнения всех полей заявки на вклад и суммы >= 10 000
     */
    @Logging(entering = true, exiting = true)
    public boolean checkRequestIn(RequestIn requestin) {
        boolean b = false;
        if (!requestin.getIsDepositRefill().isBlank() && (requestin.getIsDepositRefill().equalsIgnoreCase("да")
                || requestin.getIsDepositRefill().equalsIgnoreCase("нет"))) {
            b = true;
        } else {
            return false;
        }
        if (!requestin.getIsReductionOfDeposit().isBlank() && (requestin.getIsReductionOfDeposit().equalsIgnoreCase("да")
                || requestin.getIsReductionOfDeposit().equalsIgnoreCase("нет"))) {
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

    /**
     * Метод - отправить на сохранение заявку на вклад
     */
    @Logging(entering = true, exiting = true)
    public Integer saveRequest(Request request) {
        ResponseEntity<Integer> idRequest =
                restTemplate.postForEntity("http://localhost:8082/request/" + CustomerMicroService.getIdCustomer() + "/save",
                        request,
                        Integer.class);
        if (idRequest.getStatusCode().is2xxSuccessful()) {
            return idRequest.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Ошибка при взаимодействии с сервисом deposit " + idRequest.getBody());
        }
    }

    /**
     * Метод - отправить смс для подтверждения открытия вклада
     */
    @Logging(entering = true, exiting = true)
    public void sendRequestCode(Integer idRequest) {
        String url = "http://localhost:8082/request/sendcode/" + idRequest + "/" + CustomerMicroService.getPhoneNumber();
        ResponseEntity<Boolean> code = restTemplate.getForEntity(url, Boolean.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Ошибка при взаимодействии с сервисом deposit " + code.getBody());
        }
    }

    /**
     * Метод - проверка правильности смс кода для Request
     */
    @Logging(entering = true, exiting = true)
    public Boolean checkRequestCode(String code) {
        ResponseEntity<Boolean> isOk =
                restTemplate.getForEntity("http://localhost:8082/request/checkcode/" + code + "/" + CustomerMicroService.getIdCustomer(), Boolean.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + code);
        }
    }

    /**
     * Метод - получить данные из request для проверки на наличие суммы на счету в account
     */
    @Logging(entering = true, exiting = true)
    public RequestData getRequestData() {
        ResponseEntity<RequestData> data =
                restTemplate.getForEntity("http://localhost:8082/request/requestdata/" + CustomerMicroService.getIdCustomer(), RequestData.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + data);
        }
    }

    /**
     * Метод - присвоить заявке статус - Одобрена
     */
    @Logging(entering = true, exiting = true)
    public Boolean changeStatusOk() {
        ResponseEntity<Boolean> data =
                restTemplate.getForEntity("http://localhost:8082/request/changestatusok/" + CustomerMicroService.getIdCustomer(), Boolean.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + data);
        }
    }

    /**
     * Метод - присвоить заявке статус - Отклонена
     */
    @Logging(entering = true, exiting = true)
    public Boolean changeStatusNotOk() {
        ResponseEntity<Boolean> data =
                restTemplate.getForEntity("http://localhost:8082/request/changestatusnotok/" + CustomerMicroService.getIdCustomer(), Boolean.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + data);
        }
    }

    /**
     * Метод - запрос на создание вклада из заявки idRequest по idCustomer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public DepositSuccess createDepositByIdRequest(Integer idRequest, String numBankAccounts) {
        String url = "http://localhost:8082/deposit/createdepositbyidrequest/"
                + CustomerMicroService.getIdCustomer() + "/" + idRequest + "/" + numBankAccounts;
        ResponseEntity<DepositSuccess> data = restTemplate.getForEntity(url, DepositSuccess.class);
        if (data.getStatusCode().is2xxSuccessful()) {
            return data.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + data);
        }
    }

    /**
     * Метод - для отображения краткой информации по вкладам по idCustomer
     */
    @Logging(entering = true, exiting = true)
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
            throw new UnexpectedException("Service Deposit, Неверные данные" + dtos);
        }
    }

    /**
     * Метод - для отображения полной информации по id вклада
     */
    @Logging(entering = true, exiting = true)
    public DepositFull showFullDeposit(Integer idDeposit) {
        String url = "http://localhost:8082/deposit/showfulldeposit/" + idDeposit;
        ResponseEntity<DepositFull> dtoFull = restTemplate.getForEntity(url, DepositFull.class);
        if (dtoFull.getStatusCode().is2xxSuccessful()) {
            return dtoFull.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + dtoFull);
        }
    }

    /**
     * Метод - код для подтверждения закрытия вклада по id вклада
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public void sendDepositCodeClose(Integer idDeposit) {
        String url = "http://localhost:8082/deposit/codeclosedeposit/" + CustomerMicroService.getPhoneNumber() + "/" + idDeposit;
        ResponseEntity<String> code = restTemplate.getForEntity(url, String.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + idDeposit);
        }
    }

    /**
     * Метод - проверка кода для подтверждения закрытия вклада по id вклада
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Boolean checkcodeclosedeposit(Integer idDeposit, String code) {
        String url = "http://localhost:8082/deposit/checkcodeclosedeposit/"
                + CustomerMicroService.getPhoneNumber() + "/" + idDeposit + "/" + code;
        ResponseEntity<Boolean> isOk = restTemplate.getForEntity(url, Boolean.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Неверные данные" + code);
        }
    }


    /**
     * Метод - получить все отклоненные заявки по customerId
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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
            throw new UnexpectedException("Service Deposit, Неверные данные" + data);
        }
    }

    /**
     * Метод - удалить заявку по id => поменять её статус на "5 - Заявка удалена"
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public void deleteRequest(Integer idRequest) {
        String url = "http://localhost:8082/request/deleterequest/" + idRequest;
        ResponseEntity<Boolean> code = restTemplate.getForEntity(url, Boolean.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Ошибка при взаимодействии с сервисом deposit " + code.getBody());
        }
    }

    /**
     * Метод - закрыть вклад по id
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public CloseDeposit closeDeposit(Integer idDeposit) {
        String url = "http://localhost:8082/deposit/closedeposit/" + idDeposit;
        ResponseEntity<CloseDeposit> code = restTemplate.getForEntity(url, CloseDeposit.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            return code.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Ошибка при взаимодействии с сервисом deposit " + code.getBody());
        }
    }

    /**
     * Метод - для отображения процентной ставки от параметров: вид вклада, срок, сумма и отображения
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public BigDecimal getDepositRate(String isDepositRefill, String isReductionOfDeposit, String depositTerm, BigDecimal depositAmount) {
        String url = "http://localhost:8082/deposit/ratedeposit/" + isDepositRefill + "/" + isReductionOfDeposit + "/" + depositTerm + "/" + depositAmount;
        ResponseEntity<BigDecimal> code = restTemplate.getForEntity(url, BigDecimal.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            return code.getBody();
        } else {
            throw new UnexpectedException("Service Deposit, Ошибка при взаимодействии с сервисом deposit " + code.getBody());
        }
    }
}