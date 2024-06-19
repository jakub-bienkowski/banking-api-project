package org.example.banking.bankingapi.services.customer;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(@Nonnull final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<CustomerDTO> findById(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .map(this::mapToDto);
    }

    @Override
    public Mono<CustomerDTO> save(CustomerDTO customer) {
        return customerRepository.save(this.mapToModel(customer))
                .map(this::mapToDto);
    }

    private Customer mapToModel(CustomerDTO customer) {
        return Customer.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .accountsIds(customer.getAccountsIds())
                .build();
    }

    private CustomerDTO mapToDto(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .accountsIds(customer.getAccountsIds())
                .accounts(new ArrayList<>())
                .build();
    }

}