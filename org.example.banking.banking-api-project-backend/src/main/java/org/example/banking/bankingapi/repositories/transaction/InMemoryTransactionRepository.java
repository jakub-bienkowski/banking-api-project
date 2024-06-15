package org.example.banking.bankingapi.repositories.transaction;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.models.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "banking-api.memoryBasedConfig", matchIfMissing = true)
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<String, List<Transaction>> transactions = new ConcurrentHashMap<>();

    @Override
    public Mono<Transaction> save(@Nonnull Transaction transaction) {
        transactions.computeIfAbsent(transaction.getAccountId(), k -> new ArrayList<>()).add(transaction);
        return Mono.just(transaction);
    }

    @Override
    public Flux<Transaction> findByAccountId(String accountId) {
        return Flux.fromIterable(transactions.getOrDefault(accountId, new ArrayList<>()));
    }
}