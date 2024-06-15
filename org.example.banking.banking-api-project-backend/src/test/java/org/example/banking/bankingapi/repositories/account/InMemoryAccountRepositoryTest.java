package org.example.banking.bankingapi.repositories.account;

import org.example.banking.bankingapi.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryAccountRepositoryTest {

    private InMemoryAccountRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryAccountRepository();
    }

    @Test
    public void testSave() {
        Account account = Account.builder()
                .id("1")
                .customerId("customer1")
                .build();

        Mono<Account> saved = repository.save(account);

        StepVerifier.create(saved)
                .assertNext(savedAccount -> {
                    assertNotNull(savedAccount);
                    assertEquals("1", savedAccount.getId());
                    assertEquals("customer1", savedAccount.getCustomerId());
                })
                .verifyComplete();
    }

    @Test
    public void testFindById() {
        Account account = Account.builder()
                .id("1")
                .customerId("customer1")
                .build();

        repository.save(account).block();

        Mono<Account> found = repository.findById("1");

        StepVerifier.create(found)
                .assertNext(foundAccount -> {
                    assertNotNull(foundAccount);
                    assertEquals("1", foundAccount.getId());
                    assertEquals("customer1", foundAccount.getCustomerId());
                })
                .verifyComplete();
    }
}