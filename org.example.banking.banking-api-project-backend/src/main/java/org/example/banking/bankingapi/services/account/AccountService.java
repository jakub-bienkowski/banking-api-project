package org.example.banking.bankingapi.services.account;

import org.example.banking.bankingapi.dto.AccountDTO;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface AccountService {

    Mono<AccountDTO> createAccount(String customerId, BigDecimal initialCredit);
    Mono<AccountDTO> getAccountByCustomerId(String customerId);

}
