package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.entity.BankAccount;
import ru.mts.exception.UnexpectedException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountMicroService {
    private final RestTemplate restTemplate;
    private final CustomerMicroService customerMicroService;

    @Autowired
    public AccountMicroService(RestTemplate restTemplate, CustomerMicroService customerMicroService) {
        this.restTemplate = restTemplate;
        this.customerMicroService = customerMicroService;
    }
    //проверка есть ли необходимая сумма на счету
    public Boolean checkDataFromRequest(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        ResponseEntity<Boolean> isOk =
                restTemplate.getForEntity("http://localhost:8083/account/checksum/" + depositDebitingAccountId + "/" + depositAmount, Boolean.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + isOk);
        }
    }
    //создать счет депозита
    public BankAccount createDepositAccount(BigDecimal depositDebitingAccountId, BigDecimal depositAmount){
        ResponseEntity<BankAccount> isOk =
                restTemplate.exchange("http://localhost:8083/account/createdepaccount/" + depositDebitingAccountId + "/" + depositAmount,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BankAccount>() {
                        }
                );
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + isOk);
        }
    }


}
