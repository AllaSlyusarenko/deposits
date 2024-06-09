package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.TypesPercentPayment;
import ru.mts.exception.UnexpectedException;

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
                new ParameterizedTypeReference<List<DepositTerm>>() {}
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
                new ParameterizedTypeReference<List<TypesPercentPayment>>() {}
        );
        if (typesPercentPayments.getStatusCode().is2xxSuccessful()) {
            return typesPercentPayments.getBody();
        } else {
            throw new UnexpectedException("Ошибка при взаимодействии с сервисом deposit " + typesPercentPayments.getBody());
        }
    }

    //отправить заявку на сохранение




}
