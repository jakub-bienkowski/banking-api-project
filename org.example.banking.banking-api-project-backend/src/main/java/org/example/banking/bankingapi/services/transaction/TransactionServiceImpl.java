package org.example.banking.bankingapi.services.transaction;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.TransactionDTO;
import org.example.banking.bankingapi.models.Transaction;
import org.example.banking.bankingapi.repositories.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public Mono<TransactionDTO> findById(@Nonnull final String accountId) {
        return transactionRepository.findById(accountId)
                .map(this::mapToDto);
    }

    @Override
    public Mono<TransactionDTO> save(@Nonnull final TransactionDTO transactionDTO) {
        return transactionRepository.save(mapToModel(transactionDTO))
                .map(this::mapToDto);
    }

    private TransactionDTO mapToDto(@Nonnull final Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccountId())
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    private Transaction mapToModel(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .id(UUID.randomUUID().toString())
                .accountId(transactionDTO.getAccountId())
                .amount(transactionDTO.getAmount())
                .timestamp(LocalDateTime.now())
                .build();
    }

}