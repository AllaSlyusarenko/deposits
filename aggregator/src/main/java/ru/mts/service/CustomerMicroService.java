package ru.mts.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.exception.UnexpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerMicroService {
    private final RestTemplate restTemplate;
    @Setter
    private static String phoneNumber; //79161234567

    @Autowired
    public CustomerMicroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //вернуть id customer по телефону
    public Integer getCustomerIdByPhoneNumber() {
        ResponseEntity<Integer> id = restTemplate.getForEntity("http://localhost:8081/customer/phone/id/" + phoneNumber, Integer.class);
        if (id.getStatusCode().is2xxSuccessful()) {
            return id.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + phoneNumber);
        }
    }

    //отправить на сохранение нового кода в customer
    public void sendCode() {
        ResponseEntity<String> code = restTemplate.getForEntity("http://localhost:8081/customer/sendcode/" + phoneNumber, String.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Что-то пошло не так" + phoneNumber);
        }
    }

    //проверка правильности смс кода для входа
    public Boolean checkCodeByPhoneNumber(String code) {
        ResponseEntity<Boolean> id =
                restTemplate.getForEntity("http://localhost:8081/customer/checkcode/" + code + "/" + phoneNumber, Boolean.class);
        if (id.getStatusCode().is2xxSuccessful()) {
            return id.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + phoneNumber);
        }
    }

    //получить id счетов из customer
    //по id получить счета из account
    public List<BigDecimal> listAccountsByPhoneNumber() {
        ResponseEntity<Integer[]> ids =
                restTemplate.getForEntity("http://localhost:8081/customer/accounts/" + phoneNumber, Integer[].class);
        Integer[] idList;
        if (ids.getStatusCode().is2xxSuccessful()) {
            idList = ids.getBody();
        } else {
            throw new UnexpectedException("Что-то пошло не так" + phoneNumber);
        }
        List<BigDecimal> accounts = new ArrayList<>();
        if (idList != null && idList.length > 0) {
            for (int i = 0; i < idList.length; i++) {

                ResponseEntity<BigDecimal> response =
                        restTemplate.getForEntity("http://localhost:8083/account/id/" + i, BigDecimal.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    accounts.add(response.getBody());
                } else {
                    throw new UnexpectedException("Что-то пошло не так" + phoneNumber);
                }
            }
        }
        return accounts;
    }

}