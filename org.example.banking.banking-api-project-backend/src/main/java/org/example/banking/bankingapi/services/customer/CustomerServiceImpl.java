package org.example.banking.bankingapi.services.customer;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.repositories.customer.CustomerRepository;
import org.example.banking.bankingapi.services.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    @Autowired
    public CustomerServiceImpl(@Nonnull final CustomerRepository customerRepository,
                               @Lazy @Nonnull final AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    @Override
    public Mono<CustomerDTO> findById(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .map(this::mapToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> retrieveUserData(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .flatMap(customer -> enrichWithAccounts(customerId, customer));
    }

    @Override
    public Mono<CustomerDTO> save(CustomerDTO customer) {
        return customerRepository.save(this.mapDtoToCustomer(customer))
                .map(this::mapToCustomerDTO);
    }

    private Customer mapDtoToCustomer(CustomerDTO customer) {
        return Customer.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .accountsIds(customer.getAccountsIds())
                .build();
    }

    private Mono<CustomerDTO> enrichWithAccounts(String customerId, Customer customer) {
        Mono<CustomerDTO> customerDtoMono = Mono.just(mapToCustomerDTO(customer));
        Flux<AccountDTO> accountsFlux = accountService.getAccountsByCustomerId(customerId);

        return customerDtoMono.zipWith(accountsFlux.collectList(), (customerDto, accounts) -> {
            customerDto.getAccounts().addAll(accounts);
            return customerDto;
        });
    }

    private CustomerDTO mapToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .accounts(new ArrayList<>())
                .accountsIds(customer.getAccountsIds())
                .build();
    }

}