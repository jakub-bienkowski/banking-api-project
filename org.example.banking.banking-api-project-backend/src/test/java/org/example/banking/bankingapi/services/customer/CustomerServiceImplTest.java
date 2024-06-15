package org.example.banking.bankingapi.services.customer;


import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.repositories.customer.InMemoryCustomerRepository;
import org.example.banking.bankingapi.services.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setUp() {
        List<Customer> initialCustomers = Arrays.asList(
                Customer.builder()
                        .id("1")
                        .name("John")
                        .surname("Doe")
                        .accountsIds(Collections.emptyList())
                        .build(),
                Customer.builder()
                        .id("2")
                        .name("Jane")
                        .surname("Doe")
                        .accountsIds(Collections.emptyList())
                        .build());

        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository(initialCustomers);
        customerService = new CustomerServiceImpl(customerRepository, Mockito.mock(AccountService.class));
    }

    @Test
    public void testFindById() {
        Mono<CustomerDTO> found = customerService.findById("1");

        StepVerifier.create(found)
                .assertNext(foundCustomer -> {
                    assertNotNull(foundCustomer);
                    assertEquals("1", foundCustomer.getId());
                    assertEquals("John", foundCustomer.getName());
                    assertEquals("Doe", foundCustomer.getSurname());
                })
                .verifyComplete();
    }

    @Test
    public void testFindById_NotFound() {
        Mono<CustomerDTO> notFound = customerService.findById("3");

        StepVerifier.create(notFound)
                .expectErrorMatches(throwable -> throwable instanceof CustomerNotFoundException)
                .verify();
    }
}