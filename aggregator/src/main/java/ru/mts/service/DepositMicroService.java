package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.Request;
import ru.mts.entity.RequestIn;
import ru.mts.entity.TypesPercentPayment;
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
        Integer idCustomer = customerMicroService.getCustomerIdByPhoneNumber();
        ResponseEntity<Integer> idRequest =
                restTemplate.postForEntity("http://localhost:8082/request/" + idCustomer + "/save",
                        request,
                        Integer.class);
        if (idRequest.getStatusCode().is2xxSuccessful()) {
            return idRequest.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом customer " + idRequest.getBody());
        }
    }

    //отправить смс для подтверждения
    public Boolean sendRequestCode(Integer idRequest) {
        ResponseEntity<Boolean> code =
                restTemplate.getForEntity("http://localhost:8082/request/checkcode",
                        Boolean.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            return code.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом customer " + code.getBody());
        }
    }

    //проверка правильности смс кода для Request
    public Boolean checkRequestCode(String code) {
        Integer idCustomer = customerMicroService.getCustomerIdByPhoneNumber();
        ResponseEntity<Boolean> isOk =
                restTemplate.getForEntity("http://localhost:8082/request/checkcode/" + idCustomer, Boolean.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + code);
        }
    }


}
