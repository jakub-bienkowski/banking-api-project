package org.example.banking.bankingapi.repositories.customer;

import org.example.banking.bankingapi.models.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<Customer> findById(String id);
    Mono<Customer> save(Customer customer);

}
