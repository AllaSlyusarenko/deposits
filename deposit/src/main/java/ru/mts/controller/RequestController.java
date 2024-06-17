package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.dto.RequestDataOut;
import ru.mts.dto.RequestInDto;
import ru.mts.dto.RequestNotOkDto;
import ru.mts.service.RequestCodeServiceImpl;
import ru.mts.service.RequestServiceImpl;

import java.util.List;

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

    /**
     * Метод - создание заявки по customerId, возвращает id request
     */
    @PostMapping("/{customerId}/save")
    public ResponseEntity<Integer> saveRequest(@PathVariable(value = "customerId") Integer customerId,
                                               @RequestBody RequestInDto requestDtoIn) {

        Integer idRequest = requestService.createRequest(customerId, requestDtoIn);
        return new ResponseEntity<>(idRequest, HttpStatus.OK);
    }

    /**
     * Метод - отправить код на телефон для заявки idRequest
     */
    @GetMapping("/sendcode/{idrequest}/{phoneNumber}")
    public ResponseEntity<Boolean> sendCode(@PathVariable(value = "idrequest") Integer idRequest,
                                            @PathVariable(value = "phoneNumber") String phoneNumber) {

        String message = requestService.sendRequestCode(idRequest, phoneNumber);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Метод - проверить смс код по customerId(последняя заявка)
     */
    @GetMapping("/checkcode/{code}/{customerId}")
    public ResponseEntity<Boolean> checkCode(@PathVariable(value = "customerId") Integer customerId,
                                             @PathVariable(value = "code") String code) {

        Boolean isOk = requestService.checkRequestCode(customerId, code);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

    /**
     * Метод - получение данных из заявки для проверки достаточности суммы в account
     */
    @GetMapping("/requestdata/{customerId}")
    public ResponseEntity<RequestDataOut> getRequestData(@PathVariable(value = "customerId") Integer customerId) {

        RequestDataOut data = requestService.getRequestData(customerId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Метод - для изменения статуса заявки на одобрена после проверки суммы на счету
     */
    @GetMapping("/changestatusok/{customerId}")
    public ResponseEntity<Boolean> changeStatusOk(@PathVariable(value = "customerId") Integer customerId) {

        Boolean isOk = requestService.changeStatusOk(customerId);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

    /**
     * Метод - для изменения статуса заявки на отклонена после проверки суммы на счету
     */
    @GetMapping("/changestatusnotok/{customerId}")
    public ResponseEntity<Boolean> changeStatusNotOk(@PathVariable(value = "customerId") Integer customerId) {
        Boolean isOk = requestService.changeStatusNotOk(customerId);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

    /**
     * Метод - получить все отклоненные заявки по customerId
     */
    @GetMapping("/requestnotok/{customerId}")
    public ResponseEntity<List<RequestNotOkDto>> requestNotOk(@PathVariable(value = "customerId") Integer customerId) {

        List<RequestNotOkDto> requests = requestService.requestnotok(customerId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    /**
     * Метод - удалить заявку по id
     */
    @GetMapping("/deleterequest/{idRequest}")
    public ResponseEntity<Boolean> deleteRequest(@PathVariable(value = "idRequest") Integer idRequest) {
        Boolean isOk = requestService.deleteRequest(idRequest);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }
}