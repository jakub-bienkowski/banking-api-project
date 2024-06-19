package org.example.banking.bankingapi.repositories.transaction;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.models.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "banking-api.memoryBasedConfig", matchIfMissing = true)
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Mono<Transaction> save(@Nonnull Transaction transaction) {
        this.transactions.put(transaction.getId(), transaction);
        return Mono.just(transaction);
    }

    @Override
    public Mono<Transaction> findById(String accountId) {
        return Mono.justOrEmpty(transactions.get(accountId));
    }
}