package org.example.banking.bankingapi.services.account;

import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
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
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
        AccountService accountServiceMock = Mockito.mock(AccountService.class);

        CustomerService customerService = new CustomerServiceImpl(customerRepository, accountServiceMock);
        TransactionService transactionService = new TransactionServiceImpl(new InMemoryTransactionRepository());
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        accountService = new AccountServiceImpl(accountRepository, customerService, transactionService);
    }

    //TODO 1: Test create method!

    @Test
    public void testCustomerNotFound() {
        String nonExistentAccountId = "non-existent-customer-id";

        Flux<AccountDTO> account = accountService.getAccountsByCustomerId(nonExistentAccountId);

        StepVerifier.create(account)
                .expectErrorMatches(throwable -> throwable instanceof CustomerNotFoundException)
                .verify();
    }
}