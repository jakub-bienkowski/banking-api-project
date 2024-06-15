package org.example.banking.bankingapi.services.transaction;

import org.example.banking.bankingapi.dto.TransactionDTO;
import org.example.banking.bankingapi.repositories.transaction.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    public void testPerformTransaction() {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountId("account1")
                .amount(new BigDecimal("50.00"))
                .timestamp(LocalDateTime.now())
                .build();

        Mono<TransactionDTO> performed = transactionService.performTransaction(transactionDTO);

        StepVerifier.create(performed)
                .assertNext(performedTransaction -> {
                    assertNotNull(performedTransaction);
                    assertEquals(transactionDTO.getAccountId(), performedTransaction.getAccountId());
                    assertEquals(transactionDTO.getAmount(), performedTransaction.getAmount());
                })
                .verifyComplete();
    }

    @Test
    public void testGetTransactionsByAccountId() {
        // Perform a transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountId("account1")
                .amount(new BigDecimal("50.00"))
                .timestamp(LocalDateTime.now())
                .build();

        Mono<TransactionDTO> performed = transactionService.performTransaction(transactionDTO);

        StepVerifier.create(performed)
                .assertNext(performedTransaction -> {
                    assertNotNull(performedTransaction);
                    assertEquals(transactionDTO.getAccountId(), performedTransaction.getAccountId());
                    assertEquals(transactionDTO.getAmount(), performedTransaction.getAmount());
                })
                .verifyComplete();

        // Get transactions for the account
        Flux<TransactionDTO> transactions = transactionService.getTransactionsByAccountId("account1");

        StepVerifier.create(transactions)
                .assertNext(transaction -> {
                    assertNotNull(transaction);
                    assertEquals("account1", transaction.getAccountId());
                })
                .verifyComplete();
    }
}