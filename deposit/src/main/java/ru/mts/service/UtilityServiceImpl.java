package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.DepositRate;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.DepositsType;
import ru.mts.entity.TypesPercentPayment;
import ru.mts.repository.DepositRateRepository;
import ru.mts.repository.DepositTermRepository;
import ru.mts.repository.DepositsTypeRepository;
import ru.mts.repository.TypesPercentPaymentRepository;

import java.util.List;

@Service
public class UtilityServiceImpl {
    private final DepositTermRepository depositTermRepository;
    private final TypesPercentPaymentRepository typesPercentPaymentRepository;
    private final DepositsTypeRepository depositsTypeRepository;
    private final DepositRateRepository depositRateRepository;

    @Autowired
    public UtilityServiceImpl(DepositTermRepository depositTermRepository, TypesPercentPaymentRepository typesPercentPaymentRepository, DepositsTypeRepository depositsTypeRepository, DepositRateRepository depositRateRepository) {
        this.depositTermRepository = depositTermRepository;
        this.typesPercentPaymentRepository = typesPercentPaymentRepository;
        this.depositsTypeRepository = depositsTypeRepository;
        this.depositRateRepository = depositRateRepository;
    }

    //получить все DepositTerm 2.13
    public List<DepositTerm> getDepositTerms() {
        return depositTermRepository.findAll();
    }

    //получить все TypesPercentPayment 2.18
    public List<TypesPercentPayment> getTypesPercentPayments() {
        return typesPercentPaymentRepository.findAll();
    }

    //получить все DepositsType 2.7
    public List<DepositsType> getDepositsTypes() {
        return depositsTypeRepository.findAll();
    }

    //получить все DepositRate 2.10
    public List<DepositRate> getDepositRates() {
        return depositRateRepository.findAll();
    }
}