package org.example.banking.bankingapi.repositories.customer;

import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.DataRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends DataRepository<Customer, String> {

    Flux<Customer> getAll();

}
