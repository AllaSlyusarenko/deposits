package ru.mts.mapper;

import lombok.experimental.UtilityClass;
import ru.mts.dto.BankAccountOutDto;
import ru.mts.entity.BankAccount;

@UtilityClass
public class BankAccountMapper {
    public BankAccountOutDto bankAccountToDto(BankAccount bankAccount) {
        BankAccountOutDto bankAccountDto = new BankAccountOutDto();
        bankAccountDto.setIdBankAccounts(bankAccount.getIdBankAccounts());
        bankAccountDto.setNumBankAccounts(bankAccount.getNumBankAccounts().toString());
        bankAccountDto.setIsActive(bankAccount.getIsActive());
        return bankAccountDto;
    }
}
