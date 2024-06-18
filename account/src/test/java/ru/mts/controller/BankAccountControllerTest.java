package ru.mts.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.mts.TestDatabaseConfig;
import ru.mts.dto.BankAccountOutDto;
import ru.mts.entity.BankAccount;
import ru.mts.service.BankAccountService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = {TestDatabaseConfig.class})
@AutoConfigureMockMvc
@WebAppConfiguration
@DisplayName(value = "BankAccount methods controller tests")
class BankAccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BankAccountService bankAccountService;

    private BankAccount bankAccountSave;
    private BankAccount bankAccountOut;
    private BankAccountOutDto bankAccountOutDto;

    @BeforeEach
    void init() {
        bankAccountOut = new BankAccount();
        bankAccountOut.setIdBankAccounts(1);
        bankAccountOut.setNumBankAccounts(new BigDecimal("61234567890123456789"));
        bankAccountOut.setIsActive(true);
        bankAccountOut.setAmount(new BigDecimal("15000"));

        bankAccountOutDto = new BankAccountOutDto();
        bankAccountOutDto.setIdBankAccounts(1);
        bankAccountOutDto.setNumBankAccounts("61234567890123456789");
        bankAccountOutDto.setIsActive(true);

        bankAccountSave = new BankAccount();
        bankAccountSave.setIdBankAccounts(2);
        bankAccountSave.setNumBankAccounts(new BigDecimal("71234567890123456789"));
        bankAccountSave.setIsActive(true);
        bankAccountSave.setAmount(new BigDecimal("25000"));
    }

    @Test
    @DisplayName(value = "уменьшение суммы счета по номеру счета depositDebitingAccountId на сумму depositAmount")
    void reduceBalanceByNumBankAccounts() throws Exception {
        when(bankAccountService
                .reduceBalanceByNumBankAccounts(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class)))
                .thenReturn(bankAccountOut);

        mockMvc.perform(get("/account/reduceBalance/{depositDebitingAccountId}/{depositAmount}", new BigDecimal("61234567890123456789"), new BigDecimal("15000"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.idBankAccounts", is(bankAccountOut.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$.numBankAccounts").value(bankAccountOut.getNumBankAccounts()))
                .andExpect(jsonPath("$.amount").value(bankAccountOut.getAmount()))
                .andExpect(jsonPath("$.currency").value(bankAccountOut.getCurrency()))
                .andExpect(jsonPath("$.isActive").value(bankAccountOut.getIsActive()));
    }

    @Test
    @DisplayName(value = "получение по idAccount - активный банковский счет по idAccount и возвращает номер счета")
    void getBankAccountByIdAccount() throws Exception {
        when(bankAccountService
                .getBankAccountByIdAccount(Mockito.anyInt()))
                .thenReturn(new BigDecimal("61234567890123456789"));

        mockMvc.perform(get("/account/id/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", is(bankAccountOut.getNumBankAccounts()), BigDecimal.class));
    }


    @Test
    @DisplayName(value = "получение по id - активный счет и его номер счета")
    void getBankAccountOutDtoByIdAccount() throws Exception {
        when(bankAccountService
                .getBankAccountOutDtoByIdAccount(Mockito.anyInt()))
                .thenReturn(bankAccountOutDto);

        mockMvc.perform(get("/account/id/dto/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.idBankAccounts", is(bankAccountOut.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$.numBankAccounts", is(bankAccountOut.getNumBankAccounts()), BigDecimal.class))
                .andExpect(jsonPath("$.isActive", is(bankAccountOut.getIsActive()), Boolean.class));
    }

    @Test
    @DisplayName(value = "получение BankAccount по номеру счета")
    void getBankAccountByNum() throws Exception {
        when(bankAccountService
                .getBankAccountByNumBankAccounts(Mockito.any(BigDecimal.class)))
                .thenReturn(bankAccountOut);

        mockMvc.perform(get("/account/num/{num}", new BigDecimal("61234567890123456789"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.idBankAccounts", is(bankAccountOut.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$.numBankAccounts", is(bankAccountOut.getNumBankAccounts()), BigDecimal.class))
                .andExpect(jsonPath("$.amount", is(bankAccountOut.getAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.currency", is(bankAccountOut.getCurrency()), String.class))
                .andExpect(jsonPath("$.isActive", is(bankAccountOut.getIsActive()), Boolean.class));
    }

    @Test
    @DisplayName(value = "получение суммы по id счета")
    void getBalanceBankAccountById() throws Exception {
        when(bankAccountService
                .getBalance(Mockito.anyInt()))
                .thenReturn(new BigDecimal("15000"));

        mockMvc.perform(get("/account/amount/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", is(bankAccountOut.getAmount()), BigDecimal.class));
    }

    @Test
    @DisplayName(value = "увеличение суммы по id счета на сумму incAmount")
    void increaseBalanceBankAccountById() throws Exception {
        when(bankAccountService
                .increaseBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)))
                .thenReturn(bankAccountOut);

        mockMvc.perform(patch("/account/increase/{id}/{incAmount}", 1, new BigDecimal("10000"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.idBankAccounts", is(bankAccountOut.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$.numBankAccounts", is(bankAccountOut.getNumBankAccounts()), BigDecimal.class))
                .andExpect(jsonPath("$.amount", is(bankAccountOut.getAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.currency", is(bankAccountOut.getCurrency()), String.class))
                .andExpect(jsonPath("$.isActive", is(bankAccountOut.getIsActive()), Boolean.class));
    }

    @Test
    @DisplayName(value = "уменьшение суммы по id счета на сумму incAmount")
    void reduceBalanceBankAccountById() throws Exception {
        when(bankAccountService
                .reduceBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)))
                .thenReturn(bankAccountOut);

        mockMvc.perform(patch("/account/reduce/{id}/{redAmount}", 1, new BigDecimal("10000"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.idBankAccounts", is(bankAccountOut.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$.numBankAccounts", is(bankAccountOut.getNumBankAccounts()), BigDecimal.class))
                .andExpect(jsonPath("$.amount", is(bankAccountOut.getAmount()), BigDecimal.class))
                .andExpect(jsonPath("$.currency", is(bankAccountOut.getCurrency()), String.class))
                .andExpect(jsonPath("$.isActive", is(bankAccountOut.getIsActive()), Boolean.class));
    }

    @Test
    @DisplayName(value = "перевода суммы transferAmount по id со счета from на to")
    void transferBalanceBankAccountById() throws Exception {
        BankAccount[] bankAccounts = new BankAccount[]{bankAccountSave, bankAccountOut};

        when(bankAccountService
                .transferBalance(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(BigDecimal.class)))
                .thenReturn(bankAccounts);

        mockMvc.perform(patch("/account/transfer/{from}/{transferAmount}/{to}", 2, new BigDecimal("10000"), 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].idBankAccounts", is(bankAccountSave.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$[0].numBankAccounts", is(bankAccountSave.getNumBankAccounts()), BigDecimal.class))
                .andExpect(jsonPath("$[0].amount", is(bankAccountSave.getAmount()), BigDecimal.class))
                .andExpect(jsonPath("$[0].currency", is(bankAccountSave.getCurrency()), String.class))
                .andExpect(jsonPath("$[0].isActive", is(bankAccountSave.getIsActive()), Boolean.class))
                .andExpect(jsonPath("$[1].idBankAccounts", is(bankAccountOut.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$[1].numBankAccounts", is(bankAccountOut.getNumBankAccounts()), BigDecimal.class))
                .andExpect(jsonPath("$[1].amount", is(bankAccountOut.getAmount()), BigDecimal.class))
                .andExpect(jsonPath("$[1].currency", is(bankAccountOut.getCurrency()), String.class))
                .andExpect(jsonPath("$[1].isActive", is(bankAccountOut.getIsActive()), Boolean.class));
    }

    @Test
    @DisplayName(value = "проверка есть ли на счету необходимая сумма")
    void checkDataFromRequestSum() throws Exception {
        when(bankAccountService
                .checkDataFromRequestSum(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class)))
                .thenReturn(true);

        mockMvc.perform(get("/account/checksum/{depositDebitingAccountId}/{depositAmount}",
                        new BigDecimal("61234567890123456789"), new BigDecimal("10000"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    @DisplayName(value = "создание нового активного счета вклада с суммой на счету")
    void createDepositAccount() throws Exception {
        when(bankAccountService
                .createDepositAccount(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class)))
                .thenReturn(bankAccountOutDto);

        mockMvc.perform(get("/account/createdepaccount/{depositDebitingAccountId}/{depositAmount}",
                        new BigDecimal("61234567890123456789"), new BigDecimal("10000"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.idBankAccounts", is(bankAccountOut.getIdBankAccounts()), Integer.class))
                .andExpect(jsonPath("$.numBankAccounts", is(bankAccountOut.getNumBankAccounts()), BigDecimal.class))
                .andExpect(jsonPath("$.isActive", is(bankAccountOut.getIsActive()), Boolean.class));
    }

    @Test
    @DisplayName(value = "получение суммы на активном счете, если счет неактивный, то вернуть ноль")
    void amountByIdBankAccounts() throws Exception {
        BigDecimal amount = new BigDecimal("25000");
        when(bankAccountService
                .amountByIdBankAccounts(Mockito.any(Integer.class)))
                .thenReturn(new BigDecimal("25000"));

        mockMvc.perform(get("/account/amountbyid/{idBankAccounts}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", is(amount.intValue())));
    }

    @Test
    @DisplayName(value = "закрытии вклада => счет вклада сделать неактивным, сумму перевести на основной счет")
    void closeDeposit() throws Exception {
        when(bankAccountService
                .closeDeposit(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class)))
                .thenReturn(true);

        mockMvc.perform(get("/account/closedeposit/{depositAccountId}/{depositRefundAccountId}/{depositAmount}",
                        new BigDecimal("61234567890123456789"), new BigDecimal("71234567890123456789"), new BigDecimal("15000"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", is(true)));
    }
}