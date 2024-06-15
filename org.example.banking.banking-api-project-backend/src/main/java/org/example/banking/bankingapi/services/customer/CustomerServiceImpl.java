package org.example.banking.bankingapi.services.customer;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
                .map(this::mapToCustomerDTO);
    }

    private CustomerDTO mapToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .accountsIds(customer.getAccountsIds())
                .build();
    }

}