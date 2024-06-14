package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.repository.DepositCodeRepository;

import java.util.Random;

@Service
public class DepositCodeServiceImpl {
    private final DepositCodeRepository depositCodeRepository;

    @Autowired
    public DepositCodeServiceImpl(DepositCodeRepository depositCodeRepository) {
        this.depositCodeRepository = depositCodeRepository;
    }








    //сгенерить 4-значный код
    private int createCode() {
        int maximum = 9999;
        int minimum = 1000;
        Random rn = new Random();
        int randomNum = rn.nextInt(maximum - minimum + 1) + minimum;
        return randomNum;
    }
}
