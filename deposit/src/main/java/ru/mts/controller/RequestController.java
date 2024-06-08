package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.dto.RequestDtoIn;
import ru.mts.entity.Request;
import ru.mts.entity.RequestCode;
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
    @GetMapping("/{customerId}/save")
    public ResponseEntity<Request> sendCode(@PathVariable(value = "customerId") Integer customerId,
                                            @RequestBody RequestDtoIn requestDtoIn) {
        Request request = requestService.createRequest(customerId, requestDtoIn);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    //отправить код на телефон для заявки по customerId
    @GetMapping("/sendcode/{idrequest}/{phoneNumber}")
    public ResponseEntity<String> sendCode(@PathVariable(value = "idrequest") Integer idRequest,
                                                @PathVariable(value = "phoneNumber") String phoneNumber) {
        String message = requestService.sendRequestCode(idRequest,phoneNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
//
//    //проверить смс код по customerId
//    @GetMapping("/checkcode/{customerId}")
//    public ResponseEntity<Boolean> getTest(@PathVariable(value = "customerId") Integer customerId,
//                                           @RequestBody EnterCodeIn enterCodeIn) {
//        Boolean isOk = customerService.checkEnterCode(enterCodeIn);
//        return new ResponseEntity<>(isOk, HttpStatus.OK);
//    }

}
