package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.dto.DepositOutShortDto;
import ru.mts.dto.DepositOutSuccessDto;
import ru.mts.dto.RequestDataOut;
import ru.mts.entity.Deposit;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.TypesPercentPayment;
import ru.mts.service.DepositServiceImpl;
import ru.mts.service.UtilityServiceImpl;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/deposit")
public class DepositController {
    private final UtilityServiceImpl utilityService;
    private final DepositServiceImpl depositService;

    @Autowired
    public DepositController(UtilityServiceImpl utilityService, DepositServiceImpl depositService) {
        this.utilityService = utilityService;
        this.depositService = depositService;
    }

    //получает все DepositTerm
    @GetMapping("/alldepositterm")
    public List<DepositTerm> allDepositTerm() {
        return utilityService.getDepositTerms();
    }

    //получает все TypesPercentPayment
    @GetMapping("/alltypespercent")
    public List<TypesPercentPayment> allTypesPercentPayments() {
        return utilityService.getTypesPercentPayments();
    }

    //создает вклад по заявке idRequest и возвращает dto
    @GetMapping("/createdepositbyidrequest/{idCustomer}/{idRequest}/{numBankAccounts}")
    public ResponseEntity<DepositOutSuccessDto> createDepositByIdRequest(@PathVariable("idCustomer") Integer idCustomer,
                                                                         @PathVariable("idRequest") Integer idRequest,
                                                                         @PathVariable("numBankAccounts") BigDecimal numBankAccounts) {
        try {
            DepositOutSuccessDto depositOutSuccessDto =
                    depositService.depositOutSuccess(depositService.createDepositByIdRequest(idCustomer, idRequest, numBankAccounts));
            return new ResponseEntity<>(depositOutSuccessDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //получить все активные депозиты по idCustomer, возвращает дто
    @GetMapping("/allshortdepositsactive/{idCustomer}")
    public ResponseEntity<List<DepositOutShortDto>> allDepositsActive(@PathVariable("idCustomer") Integer idCustomer) {
        try {

            List<DepositOutShortDto> dtos = depositService.getAllDepositOutShortDtoActiveDeposits(idCustomer);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}