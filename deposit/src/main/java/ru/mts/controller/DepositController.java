package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.dto.CloseDepositDto;
import ru.mts.dto.DepositOutFullDto;
import ru.mts.dto.DepositOutShortDto;
import ru.mts.dto.DepositOutSuccessDto;
import ru.mts.entity.DepositRate;
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

    /**
     * Метод - получает все DepositTerm
     */
    @GetMapping("/alldepositterm")
    public List<DepositTerm> allDepositTerm() {
        return utilityService.getDepositTerms();
    }

    /**
     * Метод - получает все TypesPercentPayment
     */
    @GetMapping("/alltypespercent")
    public List<TypesPercentPayment> allTypesPercentPayments() {
        return utilityService.getTypesPercentPayments();
    }

    /**
     * Метод - создает вклад по заявке idRequest и возвращает dto
     */
    @GetMapping("/createdepositbyidrequest/{idCustomer}/{idRequest}/{numBankAccounts}")
    public ResponseEntity<DepositOutSuccessDto> createDepositByIdRequest(@PathVariable("idCustomer") Integer idCustomer,
                                                                         @PathVariable("idRequest") Integer idRequest,
                                                                         @PathVariable("numBankAccounts") BigDecimal numBankAccounts) {
        DepositOutSuccessDto depositOutSuccessDto =
                depositService.depositOutSuccess(depositService.createDepositByIdRequest(idCustomer, idRequest, numBankAccounts));
        return new ResponseEntity<>(depositOutSuccessDto, HttpStatus.OK);
    }

    /**
     * Метод - получить все активные депозиты по idCustomer, возвращает дто
     */
    @GetMapping("/allshortdepositsactive/{idCustomer}")
    public ResponseEntity<List<DepositOutShortDto>> allDepositsActive(@PathVariable("idCustomer") Integer idCustomer) {
        List<DepositOutShortDto> dtos = depositService.getAllDepositOutShortDtoActiveDeposits(idCustomer);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Метод - для отображения полной информации по id вклада
     */
    @GetMapping("/showfulldeposit/{idDeposit}")
    public ResponseEntity<DepositOutFullDto> showFullDeposit(@PathVariable("idDeposit") Integer idDeposit) {
        DepositOutFullDto dtos = depositService.showFullDeposit(idDeposit);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Метод - закрыть вклад по id
     */
    @GetMapping("/closedeposit/{idDeposit}")
    public ResponseEntity<CloseDepositDto> closeDeposit(@PathVariable("idDeposit") Integer idDeposit) {
        CloseDepositDto data = depositService.closeDeposit(idDeposit);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Метод - код для подтверждения закрытия вклада по id вклада
     */
    @GetMapping("/codeclosedeposit/{phoneNumber}/{idDeposit}")
    public ResponseEntity<String> sendDepositCodeClose(@PathVariable("idDeposit") Integer idDeposit,
                                                       @PathVariable("phoneNumber") String phoneNumber) {

        String code = depositService.sendDepositCodeClose(idDeposit, phoneNumber);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    /**
     * Метод - проверка кода для подтверждения закрытия вклада по id вклада
     */
    @GetMapping("/checkcodeclosedeposit/{phoneNumber}/{idDeposit}/{code}")
    public ResponseEntity<Boolean> checkCodeCloseDeposit(@PathVariable("idDeposit") Integer idDeposit,
                                                         @PathVariable("phoneNumber") String phoneNumber,
                                                         @PathVariable("code") String code) {

        Boolean isOk = depositService.checkCodeCloseDeposit(idDeposit, phoneNumber, code);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

    /**
     * Метод - определения ставки по вкладу
     */
    @GetMapping("/ratedeposit/{isDepositRefill}/{isReductionOfDeposit}/{depositTerm}/{depositAmount}")
    public ResponseEntity<BigDecimal> getDepositRateByParam(@PathVariable("isDepositRefill") String isDepositRefill,
                                                            @PathVariable("isReductionOfDeposit") String isReductionOfDeposit,
                                                            @PathVariable("depositTerm") String depositTerm,
                                                            @PathVariable("depositAmount") BigDecimal depositAmount) {
        BigDecimal rate = depositService.getDepositRate(isDepositRefill, isReductionOfDeposit, depositTerm, depositAmount);
        return new ResponseEntity<>(rate, HttpStatus.OK);
    }
}