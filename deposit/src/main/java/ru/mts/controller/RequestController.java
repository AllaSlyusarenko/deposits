package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.dto.RequestDataOut;
import ru.mts.dto.RequestInDto;
import ru.mts.dto.RequestOutDto;
import ru.mts.entity.Request;
import ru.mts.service.RequestCodeServiceImpl;
import ru.mts.service.RequestServiceImpl;

@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController {
    private final RequestServiceImpl requestService;
    private final RequestCodeServiceImpl requestCodeService;

    @Autowired
    public RequestController(RequestServiceImpl requestService, RequestCodeServiceImpl requestCodeService) {
        this.requestService = requestService;
        this.requestCodeService = requestCodeService;
    }

    //customerId, создать заявку   - возможно возвращать только id request
    @PostMapping("/{customerId}/save")
    public ResponseEntity<Integer> saveRequest(@PathVariable(value = "customerId") Integer customerId,
                                               @RequestBody RequestInDto requestDtoIn) {
        try {
            Integer idRequest = requestService.createRequest(customerId, requestDtoIn);
            return new ResponseEntity<>(idRequest, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //отправить код на телефон для заявки
    @GetMapping("/sendcode/{idrequest}/{phoneNumber}")
    public ResponseEntity<Boolean> sendCode(@PathVariable(value = "idrequest") Integer idRequest,
                                            @PathVariable(value = "phoneNumber") String phoneNumber) {
        try {
            String message = requestService.sendRequestCode(idRequest, phoneNumber);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //проверить смс код по customerId - последняя заявка
    @GetMapping("/checkcode/{code}/{customerId}")
    public ResponseEntity<Boolean> checkCode(@PathVariable(value = "customerId") Integer customerId,
                                             @PathVariable(value = "code") String code) {
        try {
            Boolean isOk = requestService.checkRequestCode(customerId, code);
            return new ResponseEntity<>(isOk, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //получение данных из заявки для проверки достаточности суммы в account
    @GetMapping("/requestdata/{customerId}")
    public ResponseEntity<RequestDataOut> getRequestData(@PathVariable(value = "customerId") Integer customerId) {
        try {
            RequestDataOut data = requestService.getRequestData(customerId);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //для изменения статуса заявки на одобрена после проверки суммы на счету
    @GetMapping("/changestatusok/{customerId}")
    public ResponseEntity<Boolean> changeStatusOk(@PathVariable(value = "customerId") Integer customerId) {
        try {
            Boolean isOk = requestService.changeStatusOk(customerId);
            return new ResponseEntity<>(isOk, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //для изменения статуса заявки на отклонена после проверки суммы на счету
    @GetMapping("/changestatusnotok/{customerId}")
    public ResponseEntity<Boolean> changeStatusNotOk(@PathVariable(value = "customerId") Integer customerId) {
        try {
            Boolean isOk = requestService.changeStatusNotOk(customerId);
            return new ResponseEntity<>(isOk, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //получить последнюю одобренную заявку по customerId
//    @GetMapping("/requestok/{customerId}")
//    public ResponseEntity<RequestOutDto> getRequestOk(@PathVariable(value = "customerId") Integer customerId) {
//        try {
//            RequestOutDto isOk = requestService.getRequestOk(customerId);
//            return new ResponseEntity<>(isOk, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }



    //ручка для получения данных из заявки для формирования вклада




//    @PostMapping("/save/{phoneNumber}")
//    public ResponseEntity<Boolean> saveRequest(@PathVariable(value = "phoneNumber") Integer phoneNumber,
//                                               @RequestBody RequestInDto requestDtoIn) {
//        Boolean isOk = requestService.saveRequest()
//
//    }

}