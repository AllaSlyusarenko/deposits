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

    /**
     * Метод - для отображения стартовой страницы
     */
    @GetMapping("/start")
    public String start(Model model) {
        PhoneNumber phoneNumber = new PhoneNumber();
        model.addAttribute("phoneNumber", phoneNumber);
        return "start";
    }

    /**
     * Метод - для отправления кода для входа
     */
    @GetMapping(value = "/start", params = "action=Отправить код для входа")
    public String entercodeparam(Model model,
                                 @RequestParam(name = "phoneNumber") String phoneNumber) {
        if (phoneNumber.isBlank()) {
            return "redirect:/start";
        }
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

    /**
     * Метод - для подтверждения кода для входа
     */
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

    /**
     * Метод - для отображения страницы deposit
     */
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

    /**
     * Метод - для отображения deposit по id
     */
    @GetMapping(value = "/showdeposit/{id}")
    public String showFullDeposit(Model model, @PathVariable("id") Integer id) {
        DepositFull deposit = depositMicroService.showFullDeposit(id);
        model.addAttribute("deposit", deposit);
        return "fulldeposit";
    }

    /**
     * Метод - для подтверждения закрытия deposit по id
     */
    @GetMapping(value = "/agreedepositoff/{id}", params = "action=Подтвердить закрытие вклада")
    public String agreeDepositOff(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("depositCode", new DepositCode());
        if (id == null) {
            return "errordepositcode";
        }
        depositMicroService.sendDepositCodeClose(id);
        return "depositcodeclose";
    }

    /**
     * Метод - для проверки кода при закрытии deposit по id
     */
    @GetMapping(value = "/depositcodeclose", params = "action=Подтвердить код")
    public String agreeDepositOff(Model model, @RequestParam(name = "id") Integer id, @RequestParam(name = "code") String code) {
        if (code == null || code.isEmpty()) {
            return "errordepositcode";
        }
        if (depositMicroService.checkcodeclosedeposit(id, code)) {
            model.addAttribute("id", id);
            return "redirect:/closedeposit/{id}";
        }
        return "errordepositcode";
    }

    /**
     * Метод - для закрытия deposit по id
     */
    @GetMapping(value = "/closedeposit/{id}")
    public String closeDeposit(Model model, @PathVariable("id") Integer id) {
        //данные из сервиса deposit для account
        CloseDeposit closeDeposit = depositMicroService.closeDeposit(id);
        accountMicroService.closeDeposit(
                closeDeposit.getDepositAccountId(), closeDeposit.getDepositRefundAccountId(), closeDeposit.getDepositAmount());
        return "redirect:/deposit";
    }

    /**
     * Метод - для возвращения к списку вкладов
     */
    @GetMapping(value = "/deposit", params = "action=Вернуться к списку вкладов")
    public String toDeposits(Model model) {
        return "redirect:/deposit";
    }

    /**
     * Метод - для удаления заявки на депозит по id
     */
    @GetMapping(value = "/deleterequest/{id}")
    public String deleteRequest(@PathVariable("id") Integer id) {
        depositMicroService.deleteRequest(id);
        return "redirect:/deposit";
    }

    /**
     * Метод - для заполнения заявки
     */
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

    /**
     * Метод - для отмены заполнения заявки
     */
    @PostMapping(value = "/request", params = "action=Назад")
    public String cancelRequest(Model model) {
        return "redirect:/deposit";
    }

    //Показать проценты
    @PostMapping(value = "/request", params = "action=Показать проценты")
    public String depositRate(Model model, RequestIn requestin) {
        //в сервисе депозитов сделать логику по выбору процента, сделать ручку
        //сюда приносить это значение
        return "rate";
    }

    /**
     * Метод - для сохранения заявки и отправки смс
     */
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

    /**
     * Метод - для ввода смс подтверждения заявки
     */
    @GetMapping("/requestcode")
    public String requestCode(Model model) {
        model.addAttribute("requestCode", new RequestCode());
        return "requestcode";
    }

    /**
     * Метод - для подтверждения кода смс заявки
     */
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

    /**
     * Метод - для выхода из аккаунта
     */
    @GetMapping(value = "/logout")
    public String logout(Model model) {
        return "redirect:/start";
    }
}