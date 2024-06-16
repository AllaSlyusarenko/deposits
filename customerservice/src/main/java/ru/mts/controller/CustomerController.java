package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.annotation.Logging;
import ru.mts.entity.Customer;
import ru.mts.service.BankAccountCustomerService;
import ru.mts.service.CustomerService;
import ru.mts.service.EnterCodeServiceImpl;

import javax.validation.constraints.Positive;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final EnterCodeServiceImpl enterCodeService;
    private final BankAccountCustomerService bankAccountCustomerService;

    @Autowired
    public CustomerController(CustomerService customerService, EnterCodeServiceImpl enterCodeService, BankAccountCustomerService bankAccountCustomerService) {
        this.customerService = customerService;
        this.enterCodeService = enterCodeService;
        this.bankAccountCustomerService = bankAccountCustomerService;
    }

    /**
     * Метод - получить Customer по id
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/id/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") @Positive Integer id) {
        Customer customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Метод - получить Customer по номеру телефона
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<Customer> getCustomerByPhoneNumber(@PathVariable(value = "phoneNumber") String phoneNumber) {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Метод - получить idCustomer по номеру телефона
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/phone/id/{phoneNumber}")
    public ResponseEntity<Integer> getIdByByPhoneNumber(@PathVariable(value = "phoneNumber") String phoneNumber) {
        Integer id = customerService.getIdByPhoneNumber(phoneNumber);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Метод - получить последний код по idCustomer
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/code/id/{customerId}")
    public ResponseEntity<String> getLastEnterCodeByIdCustomer(@PathVariable(value = "customerId") Integer customerId) {
        String code = enterCodeService.getLastEnterCodeByIdCustomer(customerId);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    /**
     * Метод - получить время последнего кода по idCustomer
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/codedatetime/id/{customerId}")
    public ResponseEntity<OffsetDateTime> getLastEnterCodeDateTimeByIdCustomer(@PathVariable(value = "customerId") Integer customerId) {
        OffsetDateTime codeDateTime = enterCodeService.getLastEnterCodeDateTimeByIdCustomer(customerId);
        return new ResponseEntity<>(codeDateTime, HttpStatus.OK);
    }

    /**
     * Метод - для отправления смс по телефону
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/sendcode/{phoneNumber}")
    public ResponseEntity<String> sendCode(@PathVariable(value = "phoneNumber") String phoneNumber) {
        String message = customerService.sendEnterCode(phoneNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Метод - для проверки смс код по phoneNumber
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/checkcode/{code}/{phoneNumber}")
    public ResponseEntity<Boolean> checkCodeByPhoneNumber(@PathVariable(value = "code") String code,
                                                          @PathVariable(value = "phoneNumber") String phoneNumber) {
        Boolean isOk = customerService.checkEnterCodeByPhoneNumber(code, phoneNumber);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

    /**
     * Метод - для получения id всех банковских счетов по phoneNumber
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/accounts/{phoneNumber}")
    public ResponseEntity<List<Integer>> getAccountsByPhoneNumber(@PathVariable(value = "phoneNumber") String phoneNumber) {
        List<Integer> ids = customerService.getAccountsByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    /**
     * Метод - для добавления по idCustomer счет вклада по idDepositAccount
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/adddepositaccount/{idCustomer}/{idDepositAccount}")
    public ResponseEntity<Void> addDepositAccountByIdAccount(@PathVariable(value = "idCustomer") Integer idCustomer,
                                                             @PathVariable(value = "idDepositAccount") Integer idDepositAccount) {
        try {
            bankAccountCustomerService.addDepositAccountByIdAccount(idCustomer, idDepositAccount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Метод - для получения списка всех счетов по idCustomer
     */
    @Logging(entering = true, exiting = true)
    @GetMapping("/allaccounts/{idCustomer}")
    public ResponseEntity<List<Integer>> getAllAccounts(@PathVariable(value = "idCustomer") Integer idCustomer) {
        try {
            List<Integer> accounts = bankAccountCustomerService.getAllAccounts(idCustomer);
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}