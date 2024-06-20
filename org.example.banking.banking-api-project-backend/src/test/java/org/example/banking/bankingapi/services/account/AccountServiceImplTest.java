package org.example.banking.bankingapi.services.account;
import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.models.Account;
import org.example.banking.bankingapi.repositories.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        String id = "1";
        Account account = Account.builder()
                .id(id)
                .customerId("1")
                .balance(BigDecimal.valueOf(100.0))
                .transactionsIds(new ArrayList<>())
                .build();

        when(accountRepository.findById(id)).thenReturn(Mono.just(account));

        StepVerifier.create(accountService.findById(id))
                .expectNextMatches(accountDTO -> accountDTO.getId().equals(id))
                .verifyComplete();
    }

    @Test
    public void testSave() {
        AccountDTO accountDTO = AccountDTO.builder()
                .id("1")
                .customerId("1")
                .balance(BigDecimal.valueOf(100.0))
                .transactionsIds(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .id(accountDTO.getId())
                .customerId(accountDTO.getCustomerId())
                .balance(accountDTO.getBalance())
                .transactionsIds(accountDTO.getTransactionsIds())
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(account));

        StepVerifier.create(accountService.save(accountDTO))
                .expectNextMatches(savedAccountDTO -> savedAccountDTO.getBalance().equals(accountDTO.getBalance()))
                .verifyComplete();
    }

}