package ru.mts.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.annotation.Logging;
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

    /**
     * Метод - получить id customer по телефону
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Integer getCustomerIdByPhoneNumber() {
        ResponseEntity<Integer> id = restTemplate.getForEntity("http://localhost:8081/customer/phone/id/" + phoneNumber, Integer.class);
        if (id.getStatusCode().is2xxSuccessful()) {
            return id.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + phoneNumber);
        }
    }

    /**
     * Метод - отправить код по номеру телефона в customer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public void sendCode() {
        ResponseEntity<String> code = restTemplate.getForEntity("http://localhost:8081/customer/sendcode/" + phoneNumber, String.class);
        if (code.getStatusCode().is2xxSuccessful()) {
            code.getBody();
        } else {
            throw new UnexpectedException("Что-то пошло не так" + phoneNumber);
        }
    }

    /**
     * Метод - проверка правильности смс кода для входа в аккаунт
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Boolean checkCodeByPhoneNumber(String code) {
        ResponseEntity<Boolean> id =
                restTemplate.getForEntity("http://localhost:8081/customer/checkcode/" + code + "/" + phoneNumber, Boolean.class);
        if (id.getStatusCode().is2xxSuccessful()) {
            return id.getBody();
        } else {
            throw new UnexpectedException("Неверные данные" + phoneNumber);
        }
    }

    /**
     * Метод - получение id счетов из customer, по id получить счета из account
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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

    /**
     * Метод - для добавления юзеру новый счет вклада
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public void addDepositAccountByIdAccount(Integer idDepositAccount) {
        ResponseEntity<Void> id =
                restTemplate.getForEntity("http://localhost:8081/customer/adddepositaccount/" + idCustomer + "/" + idDepositAccount, Void.class);
        if (!id.getStatusCode().is2xxSuccessful()) {
            throw new UnexpectedException("Неверные данные" + idDepositAccount);
        }

    }

    /**
     * Метод - для получения списка всех счетов по idCustomer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
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