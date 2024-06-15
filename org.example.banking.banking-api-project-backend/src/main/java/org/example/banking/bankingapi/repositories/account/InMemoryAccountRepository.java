package org.example.banking.bankingapi.repositories.account;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.models.Account;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "banking-api.memoryBasedConfig", matchIfMissing = true)
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> accounts =  new ConcurrentHashMap<>();


    @Override
    public Mono<Account> save(@Nonnull final Account account) {
        accounts.put(account.getCustomerId(), account);
        return Mono.just(account);
    }

    @Override
    public Mono<Account> findByCustomerId(String customerId) {
        return Mono.justOrEmpty(accounts.get(customerId));
    }

}