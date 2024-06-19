package org.example.banking.bankingapi.services.banking;

import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.dto.requests.AccountCreationRequest;
import reactor.core.publisher.Mono;

public interface BankingService {

    Mono<CustomerDTO> getCustomerDataById(String customerId);
    Mono<AccountDTO> createAccount(AccountCreationRequest request);

}
