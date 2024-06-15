package org.example.banking.bankingapi.repositories.customer;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "banking-api.memoryBasedConfig", matchIfMissing = true)
public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers =  new ConcurrentHashMap<>();

    @Autowired
    public InMemoryCustomerRepository(@Nonnull final List<Customer> initialCustomers) {
        this.fillRepository(initialCustomers);
    }

    private void fillRepository(List<Customer> initialCustomers) {
        initialCustomers.forEach(customer -> customers.put(customer.getId(), customer));
    }

    @Override
    public Mono<Customer> save(@Nonnull final Customer customer) {
        customers.put(customer.getId(), customer);
        return Mono.just(customer);
    }

    @Override
    public Mono<Customer> findById(String customerId) {
        return Mono.justOrEmpty(customers.get(customerId));
    }

}
