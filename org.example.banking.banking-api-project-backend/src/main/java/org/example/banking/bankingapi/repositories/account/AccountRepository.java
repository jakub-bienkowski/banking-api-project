package org.example.banking.bankingapi.repositories.account;

import org.example.banking.bankingapi.models.Account;
import reactor.core.publisher.Mono;

public interface AccountRepository {

    Mono<Account> save(Account account);
    Mono<Account> findByCustomerId(String customerId);

}
