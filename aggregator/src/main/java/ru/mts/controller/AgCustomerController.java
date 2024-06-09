package ru.mts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.service.CustomerMicroService;

@Slf4j
@RestController
@RequestMapping("/ag/customer")
public class AgCustomerController {
    private final CustomerMicroService customerMicroService;

    @Autowired
    public AgCustomerController(CustomerMicroService customerMicroService) {
        this.customerMicroService = customerMicroService;
    }

//    @GetMapping("/phone/id/{phoneNumber}")
//    public ResponseEntity<Integer> getIdByByPhoneNumber(@PathVariable(value = "phoneNumber") String phoneNumber) {
//        Integer id = customerMicroService.getCustomerIdByPhoneNumber(phoneNumber);
//        return new ResponseEntity<>(id, HttpStatus.OK);
//    }

}