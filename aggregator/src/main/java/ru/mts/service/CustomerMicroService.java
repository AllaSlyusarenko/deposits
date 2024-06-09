package ru.mts.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.exception.UnexpectedException;

@Service
public class CustomerMicroService {
    private final RestTemplate restTemplate;
    @Setter
    private static String phoneNumber; //79261234567
//    private Integer ID = getCustomerIdByPhoneNumber();

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
//    //проверить есть ли такой пользователь с таким телефоном
//    public Boolean checkCustomer() {
//        Integer id = getCustomerIdByPhoneNumber();
//        return id != null;
//    }

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
}