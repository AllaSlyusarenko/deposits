package ru.mts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.entity.DepositTerm;
import ru.mts.entity.TypesPercentPayment;
import ru.mts.repository.DepositTermRepository;
import ru.mts.repository.TypesPercentPaymentRepository;

import java.util.List;

@Service
public class UtilityServiceImpl {
    private final DepositTermRepository depositTermRepository;
    private final TypesPercentPaymentRepository typesPercentPaymentRepository;

    @Autowired
    public UtilityServiceImpl(DepositTermRepository depositTermRepository, TypesPercentPaymentRepository typesPercentPaymentRepository) {
        this.depositTermRepository = depositTermRepository;
        this.typesPercentPaymentRepository = typesPercentPaymentRepository;
    }

    public List<DepositTerm> getDepositTerms() {
        return depositTermRepository.findAll();
    }
    public List<TypesPercentPayment> getTypesPercentPayments() {
        return typesPercentPaymentRepository.findAll();
    }
}
