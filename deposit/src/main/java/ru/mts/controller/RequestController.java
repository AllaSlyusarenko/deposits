package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.dto.RequestCodeIn;
import ru.mts.dto.RequestInDto;
import ru.mts.entity.Request;
import ru.mts.service.RequestCodeServiceImpl;
import ru.mts.service.RequestServiceImpl;

import java.rmi.UnexpectedException;

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
    public ResponseEntity<String> sendCode(@PathVariable(value = "idrequest") Integer idRequest,
                                           @PathVariable(value = "phoneNumber") String phoneNumber) {
        String message = requestService.sendRequestCode(idRequest, phoneNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //проверить смс код по requestid
    @GetMapping("/checkcode/{requestId}")
    public ResponseEntity<Boolean> checkCode(@PathVariable(value = "requestId") Integer requestId,
                                             @RequestBody RequestCodeIn requestCodeIn) {
        Boolean isOk = requestService.checkEnterCode(requestId, requestCodeIn);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

//    @PostMapping("/save/{phoneNumber}")
//    public ResponseEntity<Boolean> saveRequest(@PathVariable(value = "phoneNumber") Integer phoneNumber,
//                                               @RequestBody RequestInDto requestDtoIn) {
//        Boolean isOk = requestService.saveRequest()
//
//    }

}