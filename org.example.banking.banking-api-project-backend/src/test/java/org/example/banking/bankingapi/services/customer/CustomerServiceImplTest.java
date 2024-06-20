package org.example.banking.bankingapi.services.customer;

import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        String id = "1";
        Customer customer = Customer.builder()
                .id(id)
                .name("John")
                .surname("Doe")
                .accountsIds(new ArrayList<>())
                .build();

        when(customerRepository.findById(id)).thenReturn(Mono.just(customer));

        StepVerifier.create(customerService.findById(id))
                .expectNextMatches(customerDTO -> customerDTO.getId().equals(id))
                .verifyComplete();
    }

    @Test
    public void testSave() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id("1")
                .name("John")
                .surname("Doe")
                .accountsIds(new ArrayList<>())
                .build();

        Customer customer = Customer.builder()
                .id(customerDTO.getId())
                .name(customerDTO.getName())
                .surname(customerDTO.getSurname())
                .accountsIds(customerDTO.getAccountsIds())
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(customer));

        StepVerifier.create(customerService.save(customerDTO))
                .expectNextMatches(savedCustomerDTO -> savedCustomerDTO.getName().equals(customerDTO.getName()))
                .verifyComplete();
    }
}