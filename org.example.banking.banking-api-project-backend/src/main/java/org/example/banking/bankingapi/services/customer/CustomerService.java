package org.example.banking.bankingapi.services.customer;

import org.example.banking.bankingapi.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<CustomerDTO> findById(String customerId);
    Mono<CustomerDTO> save(CustomerDTO customer);
    Flux<CustomerDTO> getAll();
}
