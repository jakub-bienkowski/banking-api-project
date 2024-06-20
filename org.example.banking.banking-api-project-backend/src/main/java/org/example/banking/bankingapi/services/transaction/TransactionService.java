package org.example.banking.bankingapi.services.transaction;

import org.example.banking.bankingapi.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<TransactionDTO> save(TransactionDTO transactionDTO);
    Mono<TransactionDTO> findById(String accountId);

}
