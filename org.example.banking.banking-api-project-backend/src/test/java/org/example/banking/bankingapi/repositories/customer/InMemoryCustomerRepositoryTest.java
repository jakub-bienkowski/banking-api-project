package org.example.banking.bankingapi.repositories.customer;

import org.example.banking.bankingapi.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryCustomerRepositoryTest {

    private InMemoryCustomerRepository repository;

    @BeforeEach
    public void setUp() {
        final List<Customer> initialCustomers = Arrays.asList(
                Customer.builder()
                        .id("1")
                        .name("John")
                        .surname("Doe")
                        .build(),
                Customer.builder()
                        .id("2")
                        .name("Jane")
                        .surname("Doe")
                        .build());

        repository = new InMemoryCustomerRepository(initialCustomers);
    }

    @Test
    public void testFindById() {
        Mono<Customer> customerMono = repository.findById("1");

        StepVerifier.create(customerMono)
                .assertNext(customer -> {
                    assertEquals("1", customer.getId());
                    assertEquals("John", customer.getName());
                    assertEquals("Doe", customer.getSurname());
                })
                .verifyComplete();
    }

    @Test
    public void testFindByIdNotFound() {
        Mono<Customer> customerMono = repository.findById("3");
        StepVerifier.create(customerMono)
                .verifyComplete();
    }
}