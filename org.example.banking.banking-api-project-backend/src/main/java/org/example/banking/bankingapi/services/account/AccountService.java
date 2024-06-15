package org.example.banking.bankingapi.services.account;

import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.TransactionDTO;
import org.example.banking.bankingapi.models.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface AccountService {

    Mono<AccountDTO> createAccount(String customerId, BigDecimal initialCredit);
    Flux<AccountDTO> getAccountsByCustomerId(String customerId);
    Mono<Account> updateAccountTransactions(TransactionDTO transaction);
}
