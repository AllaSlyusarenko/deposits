package ru.mts.mapper;

import lombok.experimental.UtilityClass;
import ru.mts.entity.Request;
import ru.mts.entity.RequestIn;

@UtilityClass
public class RequestMapper {
    /**
     * Метод - для преобразования RequestIn в Request
     */
    public Request requestDtoToRequest(RequestIn requestIn) {
        Request request = new Request();
        request.setDepositRefill(requestIn.getIsDepositRefill().equalsIgnoreCase("да"));
        request.setReductionOfDeposit(requestIn.getIsReductionOfDeposit().equalsIgnoreCase("да"));
        request.setDepositTerm(requestIn.getDepositTerm().getDepositTermName());
        request.setDepositAmount(requestIn.getDepositAmount());
        request.setTypesPercentPayment(requestIn.getTypesPercentPayment().getTypePercentPaymentPeriod());
        request.setDepositDebitingAccountId(requestIn.getDepositDebitingAccountId().getNumBankAccounts());
        request.setPercentPaymentAccountId(requestIn.getPercentPaymentAccountId().getNumBankAccounts());
        request.setDepositRefundAccountId(requestIn.getDepositRefundAccountId().getNumBankAccounts());
        return request;
    }
}
