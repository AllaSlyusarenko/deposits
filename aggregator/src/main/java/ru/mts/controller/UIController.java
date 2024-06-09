package ru.mts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mts.entity.*;
import ru.mts.service.CustomerMicroService;
import ru.mts.service.DepositMicroService;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class UIController {
    private final DepositMicroService depositMicroService;
    private final CustomerMicroService customerMicroService;

    @Autowired
    public UIController(DepositMicroService depositMicroService, CustomerMicroService customerMicroService) {
        this.depositMicroService = depositMicroService;
        this.customerMicroService = customerMicroService;
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
            Integer id = customerMicroService.getCustomerIdByPhoneNumber();
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
        model.addAttribute("request", new Request());
        //получение сроков депозита
        List<DepositTerm> allDepositTerm = depositMicroService.getAllDepositTerm();
        model.addAttribute("depositTerm", allDepositTerm);
        //получение типов выплат процентов
        List<TypesPercentPayment> allTypesPercentPayment = depositMicroService.getAllTypesPercentPayment();
        model.addAttribute("typesPercentPayment", allTypesPercentPayment);
        //достать активные счета по телефону
        List<BigDecimal> accounts = customerMicroService.listAccountsByPhoneNumber();
        model.addAttribute("percentPaymentAccountId", accounts);
        model.addAttribute("depositRefundAccountId", accounts);
        model.addAttribute("depositDebitingAccountId", accounts);
        return "request";
    }

    @PostMapping(value = "/request", params = "action=Назад")
    public String cancelRequest(Model model) {
        return "redirect:/deposit";
    }

//    @PostMapping(value = "/request", params = "action=Принять условия")
//    public String saveRequest(Model model, Request request) {
//    //отправить на сохранение
//        depositMicroService.saveRequest(request);
//        return "redirect:/requestcode";
//    }


//    public String requestAction(Model model, @ModelAttribute("request") Request request) {
//    }

    @GetMapping("/requestcode")
    public String requestcode(Model model) {
        return "requestcode";
    }

    @GetMapping("/rate")
    public String depositrate(Model model) {

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