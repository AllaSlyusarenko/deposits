package ru.mts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.Request;
import ru.mts.entity.TypesPercentPayment;
import ru.mts.service.DepositMicroService;

import java.util.List;

@Controller
public class UIController {
    private final DepositMicroService depositMicroService;

    @Autowired
    public UIController(DepositMicroService depositMicroService) {
        this.depositMicroService = depositMicroService;
    }

    @GetMapping("/deposit")
    public String deposit(Model model) {
        return "deposits";
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
        return "request";
    }

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

}
