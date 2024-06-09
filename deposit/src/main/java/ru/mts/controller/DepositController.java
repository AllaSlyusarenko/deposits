package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.TypesPercentPayment;
import ru.mts.service.UtilityServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/deposit")
public class DepositController {
    private final UtilityServiceImpl utilityService;

    @Autowired
    public DepositController(UtilityServiceImpl utilityService) {
        this.utilityService = utilityService;
    }
    @GetMapping("/alldepositterm")
    public List<DepositTerm> allDepositTerm() {
        return utilityService.getDepositTerms();
    }

    @GetMapping("/alltypespercent")
    public List<TypesPercentPayment> allTypesPercentPayments() {
        return utilityService.getTypesPercentPayments();
    }

}
