package org.example.banking.bankingapi.repositories.transaction;


import org.example.banking.bankingapi.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryTransactionRepository();
        transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .accountId("accountId")
                .amount(BigDecimal.TEN)
                .build();
    }

    @Test
    public void testSave() {
        StepVerifier.create(repository.save(transaction))
                .assertNext(savedTransaction -> assertEquals(transaction, savedTransaction))
                .verifyComplete();
    }

    @Test
    public void testFindByAccountId() {
        repository.save(transaction).block();

        StepVerifier.create(repository.findByAccountId(transaction.getAccountId()))
                .assertNext(retrievedTransaction -> assertEquals(transaction, retrievedTransaction))
                .verifyComplete();
    }

}