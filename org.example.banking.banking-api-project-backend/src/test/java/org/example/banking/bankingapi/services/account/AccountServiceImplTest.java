package org.example.banking.bankingapi.services.account;

import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.exceptions.AccountNotFoundException;
import org.example.banking.bankingapi.models.Account;
import org.example.banking.bankingapi.repositories.account.AccountRepository;
import org.example.banking.bankingapi.services.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    private AccountServiceImpl accountService;
    private AccountRepository accountRepository;
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        customerService = Mockito.mock(CustomerService.class);
        accountService = new AccountServiceImpl(accountRepository, customerService);
    }

    @Test
    public void testCreateAccount() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id("1")
                .name("John")
                .surname("Doe")
                .build();

        Account account = Account.builder()
                .id("1")
                .customerId("1")
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(customerService.findById(anyString())).thenReturn(Mono.just(customerDTO));
        when(accountRepository.save(any())).thenReturn(Mono.just(account));

        Mono<AccountDTO> accountDTOMono = accountService.createAccount("1", BigDecimal.valueOf(1000));

        StepVerifier.create(accountDTOMono)
                .assertNext(accountDTO -> {
                    assertEquals("1", accountDTO.getId());
                    assertEquals("1", accountDTO.getCustomerId());
                    assertEquals(BigDecimal.valueOf(1000), accountDTO.getBalance());
                })
                .verifyComplete();
    }

    @Test
    public void testGetAccountByCustomerId() {
        Account account = Account.builder()
                .id("1")
                .customerId("1")
                .balance(BigDecimal.valueOf(1000))
                .build();
        when(accountRepository.findByCustomerId(anyString())).thenReturn(Mono.just(account));

        Mono<AccountDTO> accountDTOMono = accountService.getAccountByCustomerId("1");

        StepVerifier.create(accountDTOMono)
                .assertNext(accountDTO -> {
                    assertEquals("1", accountDTO.getId());
                    assertEquals("1", accountDTO.getCustomerId());
                    assertEquals(BigDecimal.valueOf(1000), accountDTO.getBalance());
                })
                .verifyComplete();
    }

    @Test
    public void testGetAccountByCustomerIdNotFound() {
        when(accountRepository.findByCustomerId(anyString())).thenReturn(Mono.empty());

        Mono<AccountDTO> accountDTOMono = accountService.getAccountByCustomerId("3");

        StepVerifier.create(accountDTOMono)
                .expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException)
                .verify();
    }
}