package org.example.banking.bankingapi.repositories.account;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.example.banking.bankingapi.models.Account;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@ConditionalOnProperty(name = "configType", havingValue = "inMemory")
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> accounts =  new ConcurrentHashMap<>();

    @Override
    public Mono<Account> save(@Nonnull final Account account) {
        log.info("Saving account: {}", account.getId());

        accounts.put(account.getId(), account);
        return Mono.just(account);
    }

    @Override
    public Mono<Account> findById(String accountId) {
        return Mono.justOrEmpty(accounts.get(accountId));
    }

}