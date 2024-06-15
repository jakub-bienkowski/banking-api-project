package org.example.banking.bankingapi.services.transaction;

import org.example.banking.bankingapi.dto.TransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface TransactionService {

    Mono<TransactionDTO> performTransaction(TransactionDTO transactionDTO);
    Flux<TransactionDTO> getTransactionsForAccount(String accountId);

}
