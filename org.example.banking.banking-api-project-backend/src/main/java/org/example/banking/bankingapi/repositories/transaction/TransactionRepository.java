package org.example.banking.bankingapi.repositories.transaction;

import org.example.banking.bankingapi.models.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository {

    Mono<Transaction> save(Transaction transaction);
    Flux<Transaction> findByAccountId(String accountId);

}
