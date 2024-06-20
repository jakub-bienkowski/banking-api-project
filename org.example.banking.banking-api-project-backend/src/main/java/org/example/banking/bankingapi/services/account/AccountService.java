package org.example.banking.bankingapi.services.account;

import org.example.banking.bankingapi.dto.AccountDTO;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<AccountDTO> save(AccountDTO account);
    Mono<AccountDTO> findById(String id);
}
