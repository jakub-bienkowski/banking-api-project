package org.example.banking.bankingapi.services.transaction;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.TransactionDTO;
import org.example.banking.bankingapi.models.Transaction;
import org.example.banking.bankingapi.repositories.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(@Nonnull final TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Mono<TransactionDTO> performTransaction(@Nonnull final TransactionDTO transactionDTO) {
        return this.saveTransaction(transactionDTO)
                .map(this::mapToTransactionDTO);
    }

    public Mono<Transaction> saveTransaction(@Nonnull final TransactionDTO transactionDTO) {
        final Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .accountId(transactionDTO.getAccountId())
                .amount(transactionDTO.getAmount())
                .timestamp(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    private TransactionDTO mapToTransactionDTO(@Nonnull final Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccountId())
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByAccountId(@Nonnull final String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .map(this::mapToTransactionDTO);
    }
}