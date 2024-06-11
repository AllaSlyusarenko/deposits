package ru.mts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mts.entity.*;
import ru.mts.mapper.RequestMapper;
import ru.mts.service.AccountMicroService;
import ru.mts.service.CustomerMicroService;
import ru.mts.service.DepositMicroService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UIController {
    private final DepositMicroService depositMicroService;
    private final CustomerMicroService customerMicroService;
    private final AccountMicroService accountMicroService;

    @Autowired
    public UIController(DepositMicroService depositMicroService, CustomerMicroService customerMicroService, AccountMicroService accountMicroService) {
        this.depositMicroService = depositMicroService;
        this.customerMicroService = customerMicroService;
        this.accountMicroService = accountMicroService;
    }

    @GetMapping("/start")
    public String start(Model model) {
        model.addAttribute("phoneNumber", new PhoneNumber());
        return "start";
    }

    // с параметрами
    @GetMapping(value = "/start", params = "action=Отправить код для входа")
    public String entercodeparam(Model model,
                                 @RequestParam(name = "phoneNumber") String phoneNumber) {
        model.addAttribute("enterCode", new EnterCode());
        CustomerMicroService.setPhoneNumber(phoneNumber);
        try {
            CustomerMicroService.setIdCustomer(customerMicroService.getCustomerIdByPhoneNumber());
        } catch (Exception e) {
            return "redirect:/start";
        }
        //отправить код
        customerMicroService.sendCode();
        return "entercode";
    }

    @GetMapping(value = "/entercode", params = "action=Подтвердить код")
    public String entercode(Model model,
                            @RequestParam(name = "code") String code) {
        //проверка правильности кода если верно то депозит, иначе опять код
        if (code == null || code.isEmpty()) {
            return "errorentercode";
        }
        if (customerMicroService.checkCodeByPhoneNumber(code)) {
            return "redirect:/deposit";
        }
        return "errorentercode";
    }

    @GetMapping("/deposit")
    public String deposit(Model model) {
        return "deposit";
    }

    @GetMapping("/request")
    public String request(Model model) {
        model.addAttribute("requestin", new RequestIn());
        //получение сроков депозита
        List<DepositTerm> allDepositTerm = depositMicroService.getAllDepositTerm();
        model.addAttribute("depositTerm", allDepositTerm);
        //получение типов выплат процентов
        List<TypesPercentPayment> allTypesPercentPayment = depositMicroService.getAllTypesPercentPayment();
        model.addAttribute("typesPercentPayment", allTypesPercentPayment);
        //достать активные счета по телефону
        List<BankAccount> accounts = customerMicroService.listAccountsByPhoneNumber();
        model.addAttribute("depositDebitingAccountId", accounts);
        model.addAttribute("percentPaymentAccountId", accounts);
        model.addAttribute("depositRefundAccountId", accounts);
        return "request";
    }

    @PostMapping(value = "/request", params = "action=Назад")
    public String cancelRequest(Model model) {
        return "redirect:/deposit";
    }

    @PostMapping(value = "/request", params = "action=Принять условия")
    public String saveRequest(Model model, RequestIn requestin) {

        //проверить заполнены ли все поля и правильность их заполнения(да/нет), сумма>=10000
        if (!depositMicroService.checkRequestIn(requestin)) {
            return "requesterror";

        }
        Request request = RequestMapper.requestDtoToRequest(requestin);
        //отправить на сохранение
        Integer idRequest = depositMicroService.saveRequest(request);
        //отправить смс для подтверждения
        depositMicroService.sendRequestCode(idRequest);
        return "redirect:/requestcode";
    }

    @GetMapping("/requestcode")
    public String requestCode(Model model) {
        model.addAttribute("requestCode", new RequestCode());
        return "requestcode";
    }

    @GetMapping(value = "/requestcode", params = "action=Подтвердить код")
    public String checkRequestCode(Model model,
                                   @RequestParam(name = "code") String code) {
        //проверка правильности кода если верно то депозит, иначе опять код
        if (code == null || code.isEmpty()) {
            return "errorrequestcode";
        }
        //проверить - отправить код на проверку
        if (depositMicroService.checkRequestCode(code)) {
            //данные из заявки - счет списания, сумма
            RequestData data = depositMicroService.getRequestData();
            //отправить запрос есть ли сумма на счету
            if(accountMicroService.checkDataFromRequest(data.getDepositDebitingAccountId(),data.getDepositAmount())){
                //при проверке - если да, то поменять статус заявки на одобрена idRequest
                depositMicroService.changeStatusOk();
                //получение последней заявки со статусом одобрена
                //номер заявки
                //вид вклада
                //сумма
                //текущая дата
                //процентная ставка

                //отправить account по данным data - data.getDepositDebitingAccountId(),data.getDepositAmount()
                //1) открыть новый счет вклада
                //2) с data.getDepositDebitingAccountId()
                //3) перечислить сумму data.getDepositAmount() на новый счет вклада



                //запрос на открытие вклада из заявки
                // создание счета для вклада
                // перечисление суммы на счет вклад и списание с основного счета
                //у юзера добавить счет вклада
                return "requestsuccess";
            }
        }
        //статус - заявка отклонена
        depositMicroService.changeStatusNotOk();
        return "errorrequestcode";
    }


    //подтвердить пароль

    @GetMapping("/rate")
    public String depositRate(Model model) {

        //в сервисе депозитов сделать логику по выбору процента, сделать ручку
        //сюда приносить это значение
        return "rate";
    }

    @GetMapping(value = "/logout")
//    @PreAuthorize("hasRole('USER')")
    public String logout(Model model) {
        return "redirect:/start";
    }

}
