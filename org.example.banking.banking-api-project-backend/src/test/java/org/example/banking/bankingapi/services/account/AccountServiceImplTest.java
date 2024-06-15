package org.example.banking.bankingapi.services.account;

import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.exceptions.AccountNotFoundException;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.models.Account;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.account.InMemoryAccountRepository;
import org.example.banking.bankingapi.repositories.customer.InMemoryCustomerRepository;
import org.example.banking.bankingapi.repositories.transaction.InMemoryTransactionRepository;
import org.example.banking.bankingapi.services.customer.CustomerService;
import org.example.banking.bankingapi.services.customer.CustomerServiceImpl;
import org.example.banking.bankingapi.services.transaction.TransactionService;
import org.example.banking.bankingapi.services.transaction.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountServiceImplTest {

    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        List<Customer> initialCustomers = Arrays.asList(
                Customer.builder()
                        .id("1")
                        .name("John")
                        .surname("Doe")
                        .accountsIds(Collections.singletonList("account1"))
                        .build(),
                Customer.builder()
                        .id("2")
                        .name("Jane")
                        .surname("Doe")
                        .accountsIds(Collections.singletonList("account2"))
                        .build());


        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository(initialCustomers);
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        TransactionService transactionService = new TransactionServiceImpl(new InMemoryTransactionRepository());
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        accountService = new AccountServiceImpl(accountRepository, customerService, transactionService);
    }

    @Test
    public void testCreateAccount() {
        AccountDTO accountDTO = AccountDTO.builder()
                .customerId("1")
                .balance(new BigDecimal("50.00"))
                .build();

        Mono<AccountDTO> created = accountService.createAccount("1", new BigDecimal("50.00"));

        StepVerifier.create(created)
                .assertNext(createdAccount -> {
                    assertNotNull(createdAccount);
                    assertEquals(accountDTO.getCustomerId(), createdAccount.getCustomerId());
                    assertEquals(accountDTO.getBalance(), createdAccount.getBalance());
                })
                .verifyComplete();
    }

    @Test
    public void testCreateAccountWithoutInitialBalance() {
        AccountDTO accountDTO = AccountDTO.builder()
                .customerId("1")
                .balance(BigDecimal.ZERO)
                .build();

        Mono<AccountDTO> created = accountService.createAccount("1", BigDecimal.ZERO);

        StepVerifier.create(created)
                .assertNext(createdAccount -> {
                    assertNotNull(createdAccount);
                    assertEquals(accountDTO.getCustomerId(), createdAccount.getCustomerId());
                    assertEquals(0, accountDTO.getBalance().compareTo(createdAccount.getBalance()));
                })
                .verifyComplete();
    }

    @Test
    public void testCustomerNotFound() {
        String nonExistentAccountId = "non-existent-customer-id";

        Flux<AccountDTO> account = accountService.getAccountsByCustomerId(nonExistentAccountId);

        StepVerifier.create(account)
                .expectErrorMatches(throwable -> throwable instanceof CustomerNotFoundException)
                .verify();
    }
}