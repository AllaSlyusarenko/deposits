package ru.mts.mapper;

import lombok.experimental.UtilityClass;
import ru.mts.entity.Request;
import ru.mts.entity.RequestIn;

@UtilityClass
public class RequestMapper {
    public Request requestDtoToRequest(RequestIn requestIn) {
        Request request = new Request();
        request.setDepositRefill(requestIn.getIsDepositRefill().equals("да"));
        request.setReductionOfDeposit(requestIn.getIsReductionOfDeposit().equals("да"));
        request.setDepositTerm(requestIn.getDepositTerm().getDepositTermName());
        request.setDepositAmount(requestIn.getDepositAmount());
        request.setTypesPercentPayment(requestIn.getTypesPercentPayment().getTypePercentPaymentPeriod());
        request.setDepositDebitingAccountId(requestIn.getDepositDebitingAccountId().getNumBankAccounts());
        request.setPercentPaymentAccountId(requestIn.getPercentPaymentAccountId().getNumBankAccounts());
        request.setDepositRefundAccountId(requestIn.getDepositRefundAccountId().getNumBankAccounts());
        return request;
    }
}
