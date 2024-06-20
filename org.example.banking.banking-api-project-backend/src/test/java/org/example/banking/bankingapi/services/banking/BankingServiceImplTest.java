package org.example.banking.bankingapi.services.banking;

import org.example.banking.bankingapi.dto.requests.AccountCreationRequest;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.account.InMemoryAccountRepository;
import org.example.banking.bankingapi.repositories.customer.InMemoryCustomerRepository;
import org.example.banking.bankingapi.repositories.transaction.InMemoryTransactionRepository;
import org.example.banking.bankingapi.services.account.AccountService;
import org.example.banking.bankingapi.services.account.AccountServiceImpl;
import org.example.banking.bankingapi.services.customer.CustomerService;
import org.example.banking.bankingapi.services.customer.CustomerServiceImpl;
import org.example.banking.bankingapi.services.transaction.TransactionService;
import org.example.banking.bankingapi.services.transaction.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class BankingServiceImplTest {

    @InjectMocks
    private BankingServiceImpl bankingService;

    @BeforeEach
    public void setup() {
        List<Customer> initialCustomers = new ArrayList<>();
        initialCustomers.add(Customer.builder()
                .id("1")
                .name("John")
                .surname("Doe")
                .accountsIds(new ArrayList<>())
                .build());

        CustomerService customerService = new CustomerServiceImpl(new InMemoryCustomerRepository(initialCustomers));
        AccountService accountService = new AccountServiceImpl(new InMemoryAccountRepository());
        TransactionService transactionService = new TransactionServiceImpl(new InMemoryTransactionRepository());
        this.bankingService = new BankingServiceImpl(accountService, customerService, transactionService);
    }

    @Test
    public void testGetCustomerDataById() {
        StepVerifier.create(bankingService.getCustomerDataById("1"))
                .expectNextMatches(returnedCustomerDTO -> returnedCustomerDTO.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    public void testCreateAccountWithInitialCredit() {
        String customerId = "1";
        BigDecimal initialCredit = BigDecimal.valueOf(100.0);
        AccountCreationRequest request = new AccountCreationRequest(customerId, initialCredit);

        StepVerifier.create(bankingService.createAccount(request))
                .expectNextMatches(returnedAccountDTO -> returnedAccountDTO.getBalance().equals(initialCredit))
                .verifyComplete();
    }

    @Test
    public void testCreateAccountWithoutInitialCredit() {
        String customerId = "1";
        AccountCreationRequest request = new AccountCreationRequest(customerId, null);

        StepVerifier.create(bankingService.createAccount(request))
                .expectNextMatches(returnedAccountDTO -> returnedAccountDTO.getBalance().equals(BigDecimal.ZERO))
                .verifyComplete();
    }
}