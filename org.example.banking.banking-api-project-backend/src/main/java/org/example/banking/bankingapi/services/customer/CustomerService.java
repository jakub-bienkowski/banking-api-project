package org.example.banking.bankingapi.services.customer;

import org.example.banking.bankingapi.dto.CustomerDTO;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<CustomerDTO> findById(String customerId);

}
