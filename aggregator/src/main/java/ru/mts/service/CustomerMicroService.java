package ru.mts.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.entity.BankAccount;
import ru.mts.exception.UnexpectedException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerMicroService {
    private final RestTemplate restTemplate;
    @Setter
    @Getter
    private static String phoneNumber; //79161234567
    @Setter
    @Getter
    private static Integer idCustomer; //1


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
    public List<BankAccount> listAccountsByPhoneNumber() {
        //получить все id account по PhoneNumber
        ResponseEntity<List<Integer>> ids =
                restTemplate.exchange("http://localhost:8081/customer/accounts/" + phoneNumber,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Integer>>() {
                        }
                );
        List<Integer> idList;
        if (ids.getStatusCode().is2xxSuccessful()) {
            idList = ids.getBody();
        } else {
            throw new UnexpectedException("Что-то пошло не так" + phoneNumber);
        }
        List<BankAccount> accounts = new ArrayList<>();
        //получить по id - активные счета и их номер счета
        if (idList != null && !idList.isEmpty()) {
            for (Integer id : idList) {
                ResponseEntity<BankAccount> response =
                        restTemplate.exchange("http://localhost:8083/account/id/dto/" + id,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<BankAccount>() {
                                }
                        );
                if (response.getStatusCode().is2xxSuccessful()) {
                    if (response.getBody().getIsActive().equals(true)) {
                        accounts.add(response.getBody());
                    }
                } else {
                    throw new UnexpectedException("Что-то пошло не так" + phoneNumber);
                }
            }
        }
        return accounts;
    }

    //у юзера добавить счет вклада
    public void addDepositAccountByIdAccount(Integer idDepositAccount) {
        ResponseEntity<Void> id =
                restTemplate.getForEntity("http://localhost:8081/customer/adddepositaccount/" + idCustomer + "/" + idDepositAccount, Void.class);
        if (!id.getStatusCode().is2xxSuccessful()) {
            throw new UnexpectedException("Неверные данные" + idDepositAccount);
        }

    }

    //список всех счетов по idCustomer
    public List<Integer> getAllAccounts() {
        ResponseEntity<List<Integer>> ids =
                restTemplate.exchange("http://localhost:8081/customer/allaccounts/" + idCustomer,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Integer>>() {
                        }
                );
        if (ids.getStatusCode().is2xxSuccessful()) {
            return ids.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + idCustomer);
        }
    }
}