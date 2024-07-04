package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mts.annotation.Logging;
import ru.mts.entity.BankAccount;
import ru.mts.exception.UnexpectedException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountMicroService {
    private final RestTemplate restTemplate;

    @Autowired
    public AccountMicroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Метод - для проверки есть ли необходимая сумма на счету
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Boolean checkDataFromRequest(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        ResponseEntity<Boolean> isOk =
                restTemplate.getForEntity("http://localhost:8083/account/checksum/" + depositDebitingAccountId + "/" + depositAmount, Boolean.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Service Account, Неверные данные" + isOk);
        }
    }

    /**
     * Метод - для создания счета депозита
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public BankAccount createDepositAccount(BigDecimal depositDebitingAccountId, BigDecimal depositAmount) {
        ResponseEntity<BankAccount> isOk =
                restTemplate.getForEntity("http://localhost:8083/account/createdepaccount/" + depositDebitingAccountId + "/" + depositAmount, BankAccount.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Service Account, Неверные данные" + isOk);
        }
    }

    /**
     * Метод - для получения общей суммы на всех активных счетах по idCustomer
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public BigDecimal totalSumAllActiveAccounts(List<Integer> ids) {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (Integer id : ids) {
            ResponseEntity<BigDecimal> sum =
                    restTemplate.getForEntity("http://localhost:8083/account/amountbyid/" + id, BigDecimal.class);
            if (sum.getStatusCode().is2xxSuccessful()) {
                totalSum = totalSum.add(sum.getBody());
            } else {
                throw new UnexpectedException("Service Account, Неверные данные" + id);
            }
        }
        return totalSum;
    }

    /**
     * Метод - для закрытия для операций счет вклада и вернуть сумму на основной счет
     */
    @Logging(entering = true, exiting = true, logArgs = true)
    public Boolean closeDeposit(BigDecimal depositAccountId, BigDecimal depositRefundAccountId, BigDecimal depositAmount) {
        String url = "http://localhost:8083/account/closedeposit/" + depositAccountId + "/" + depositRefundAccountId + "/" + depositAmount;
        ResponseEntity<Boolean> isOk =
                restTemplate.getForEntity(url, Boolean.class);
        if (isOk.getStatusCode().is2xxSuccessful()) {
            return isOk.getBody();
        } else {
            throw new UnexpectedException("Service Account, Неверные данные" + isOk);
        }
    }
}