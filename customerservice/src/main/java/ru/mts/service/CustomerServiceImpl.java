package ru.mts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.dto.EnterCodeIn;
import ru.mts.entity.Customer;
import ru.mts.entity.EnterCode;
import ru.mts.exception.NotFoundException;
import ru.mts.exception.ValidationException;
import ru.mts.repository.CustomerRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final EnterCodeServiceImpl enterCodeService;
    private final BankAccountCustomerService bankAccountCustomerService;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, EnterCodeServiceImpl enterCodeService, BankAccountCustomerService bankAccountCustomerService) {
        this.customerRepository = customerRepository;
        this.enterCodeService = enterCodeService;
        this.bankAccountCustomerService = bankAccountCustomerService;
    }

    //вернуть клиента по id
    @Override
    public Customer getCustomerById(Integer id) {
        checkId(id);
        return customerRepository.findByIdCustomers(id).orElseThrow(()
                -> new NotFoundException("Клиент " + id + " не найден"));
    }

    //вернуть клиента по номеру телефона
    @Override
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        checkPhoneNumber(phoneNumber);
        return customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(()
                -> new NotFoundException("Клиент с номером телефона " + phoneNumber + " не найден"));
    }

    //вернуть id customer по телефону
    @Override
    public Integer getIdByPhoneNumber(String phoneNumber) {
        return getCustomerByPhoneNumber(phoneNumber).getIdCustomers();
    }

    @Override
    public String sendEnterCode(String phoneNumber) {
        checkPhoneNumber(phoneNumber);
        Integer customerId = getIdByPhoneNumber(phoneNumber);
        EnterCode enterCode = enterCodeService.saveEnterCode(customerId);
        String message = "Пользователю на номер " + phoneNumber + " отправлено смс с кодом";
        log.info(message);
        log.info(enterCode.getCode());
        return message;
    }

//    //проверить смс код по customerId
//    @Override
//    public boolean checkEnterCode(EnterCodeIn enterCodeIn) {
//        Integer id = enterCodeIn.getIdCustomer();
//        checkId(id);
//        String lastCode = enterCodeService.getLastEnterCodeByIdCustomer(id);
//        OffsetDateTime lastDateTime = enterCodeService.getLastEnterCodeDateTimeByIdCustomer(id);
//        return lastCode.equals(enterCodeIn.getCode()) &&
//                enterCodeIn.getCodeDateTime().isAfter(lastDateTime) && enterCodeIn.getCodeDateTime().isBefore(lastDateTime.plusMinutes(1));
//    }

    //проверить смс код по phoneNumber
    @Override
    public boolean checkEnterCodeByPhoneNumber(String code, String phoneNumber) {
        Integer id = getIdByPhoneNumber(phoneNumber);
        EnterCodeIn enterCodeIn = new EnterCodeIn();
        enterCodeIn.setCode(code);
        enterCodeIn.setCodeDateTime(OffsetDateTime.now());
        enterCodeIn.setIdCustomer(id);
        String lastCode = enterCodeService.getLastEnterCodeByIdCustomer(id);
        OffsetDateTime lastDateTime = enterCodeService.getLastEnterCodeDateTimeByIdCustomer(id);
        return lastCode.equals(code) &&
                enterCodeIn.getCodeDateTime().isAfter(lastDateTime) && enterCodeIn.getCodeDateTime().isBefore(lastDateTime.plusMinutes(1));
    }

    //получить id банковских счетов по phoneNumber
    @Override
    public List<Integer> getAccountsByPhoneNumber(String phoneNumber) {
        Integer idCustomer = getIdByPhoneNumber(phoneNumber);
        return bankAccountCustomerService.findAllByCustomerId(idCustomer);
    }

    //проверка id
    private boolean checkId(Integer id) {
        if (id <= 0) {
            throw new ValidationException("Неверный id " + id);
        }
        return true;
    }

    //проверка номера телефона
    private boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank() || phoneNumber.length() != 11) {
            throw new ValidationException("Неверный номер телефона " + phoneNumber);
        }
        return true;
    }

    //проверка id банковского счета
    private boolean checkBankAccountId(BigDecimal bankAccountId) {
        if (bankAccountId.toString().isBlank() || bankAccountId.toString().length() != 20) {
            throw new ValidationException("Неверный номер счета " + bankAccountId);
        }
        return true;
    }
}