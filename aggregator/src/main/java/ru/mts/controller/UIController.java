package ru.mts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mts.entity.*;
import ru.mts.mapper.RequestMapper;
import ru.mts.service.AccountMicroService;
import ru.mts.service.CustomerMicroService;
import ru.mts.service.DepositMicroService;

import java.math.BigDecimal;
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
        //суммарно на активных счетах
        BigDecimal totalSum = accountMicroService.totalSumAllActiveAccounts(customerMicroService.getAllAccounts());
        model.addAttribute("totalSum", totalSum);
        //активные вклады
        List<DepositShort> depositShorts = depositMicroService.getAllDepositShortActiveDeposits();
        model.addAttribute("depositShorts", depositShorts);
        //отклоненные заявки
        List<RequestNotOk> requestNotOks = depositMicroService.getRequestNotOk();
        model.addAttribute("requestNotOks", requestNotOks);

        return "deposit";
    }

    @GetMapping(value = "/showdeposit/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public String showFullDeposit(Model model, @PathVariable("id") Integer id) {
        DepositFull deposit = depositMicroService.showFullDeposit(id);
        model.addAttribute("deposit", deposit);
        return "fulldeposit";
    }

    @GetMapping(value = "/deposit", params = "action=Вернуться к списку вкладов")
    public String toDeposits(Model model) {
        return "redirect:/deposit";
    }

    @GetMapping(value = "/deleterequest/{id}")
    public String deleteRequest(@PathVariable("id") Integer id) {
        depositMicroService.deleteRequest(id);

        return "redirect:/deposit";
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

    //Показать проценты
    @PostMapping(value = "/request", params = "action=Показать проценты")
//     @GetMapping("/rate")
    public String depositRate(Model model, RequestIn requestin) {

        //в сервисе депозитов сделать логику по выбору процента, сделать ручку
        //сюда приносить это значение
        return "rate";
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
        if (code == null || code.isEmpty()) {
            return "errorrequestcode";
        }
        if (depositMicroService.checkRequestCode(code)) {
            RequestData data = depositMicroService.getRequestData();
            if (accountMicroService.checkDataFromRequest(data.getDepositDebitingAccountId(), data.getDepositAmount())) {
                depositMicroService.changeStatusOk();
                BigDecimal id = data.getDepositDebitingAccountId();
                BigDecimal amount = data.getDepositAmount();

                //счет вклада
                BankAccount bankAccount = accountMicroService.createDepositAccount(data.getDepositDebitingAccountId(), data.getDepositAmount());
                customerMicroService.addDepositAccountByIdAccount(bankAccount.getIdBankAccounts());

                //id заявки
                Integer idRequest = data.getId();

                //вернуть dto вклада из него вывести значения в страницу успеха
                DepositSuccess depositSuccess = depositMicroService.createDepositByIdRequest(idRequest, bankAccount.getNumBankAccounts());
                model.addAttribute("depositSuccess", depositSuccess);

                return "requestsuccess";
            }
        }
        //статус - заявка отклонена
        depositMicroService.changeStatusNotOk();
        return "errorrequestcode";
    }

    @GetMapping(value = "/logout")
//    @PreAuthorize("hasRole('USER')")
    public String logout(Model model) {
        return "redirect:/start";
    }

}
