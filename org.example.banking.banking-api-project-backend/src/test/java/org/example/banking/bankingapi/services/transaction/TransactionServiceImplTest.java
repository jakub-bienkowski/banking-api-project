package org.example.banking.bankingapi.services.transaction;

import org.example.banking.bankingapi.dto.TransactionDTO;
import org.example.banking.bankingapi.models.Transaction;
import org.example.banking.bankingapi.repositories.transaction.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        String id = UUID.randomUUID().toString();
        Transaction transaction = Transaction.builder()
                .id(id)
                .accountId("1")
                .amount(BigDecimal.valueOf(100.0))
                .timestamp(LocalDateTime.now())
                .build();

        when(transactionRepository.findById(id)).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.findById(id))
                .expectNextMatches(transactionDTO -> transactionDTO.getId().equals(id))
                .verifyComplete();
    }

    @Test
    public void testSave() {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountId("1")
                .amount(BigDecimal.valueOf(100.0))
                .build();

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .accountId(transactionDTO.getAccountId())
                .amount(transactionDTO.getAmount())
                .timestamp(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.save(transactionDTO))
                .expectNextMatches(savedTransactionDTO -> savedTransactionDTO.getAmount().equals(transactionDTO.getAmount()))
                .verifyComplete();
    }
}