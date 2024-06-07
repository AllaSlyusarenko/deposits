package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.exception.UnexpectedException;

@Service
public class CustomerMicroService {
    private final RestTemplate restTemplate;

    @Autowired
    public CustomerMicroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //вернуть id customer по телефону
    public Integer getCustomerIdByPhoneNumber(String phoneNumber) {
        ResponseEntity<Integer> id = restTemplate.getForEntity("http://localhost:8081/customer/phone/id/" + phoneNumber, Integer.class);
        if (id.getStatusCode().is2xxSuccessful()) {
            return id.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + phoneNumber);
        }
    }
}