package org.example.banking.bankingapi.services.customer;

import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    public void testFindById() {
        Customer customer = Customer.builder()
                .id("1")
                .name("John")
                .surname("Doe")
                .build();
        when(customerRepository.findById(anyString())).thenReturn(Mono.just(customer));

        Mono<CustomerDTO> customerDTOMono = customerService.findById("1");

        StepVerifier.create(customerDTOMono)
                .assertNext(customerDTO -> {
                    assertEquals("1", customerDTO.getId());
                    assertEquals("John", customerDTO.getName());
                    assertEquals("Doe", customerDTO.getSurname());
                })
                .verifyComplete();
    }

    @Test
    public void testFindByIdNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Mono.empty());

        Mono<CustomerDTO> customerDTOMono = customerService.findById("3");

        StepVerifier.create(customerDTOMono)
                .expectErrorMatches(throwable -> throwable instanceof CustomerNotFoundException)
                .verify();
    }
}