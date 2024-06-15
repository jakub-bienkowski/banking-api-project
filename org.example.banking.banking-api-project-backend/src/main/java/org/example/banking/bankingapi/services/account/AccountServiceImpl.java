package org.example.banking.bankingapi.services.account;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.exceptions.AccountNotFoundException;
import org.example.banking.bankingapi.models.Account;
import org.example.banking.bankingapi.repositories.account.AccountRepository;
import org.example.banking.bankingapi.services.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    @Autowired
    public AccountServiceImpl(@Nonnull final AccountRepository accountRepository,
                              @Nonnull final  CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
    }

    @Override
    public Mono<AccountDTO> createAccount(@Nonnull final String customerId, @Nonnull final BigDecimal initialCredit) {
        return customerService.findById(customerId)
                .flatMap(customer -> this.saveAccount(customer, initialCredit))
                .map(this::mapToAccountDTO);
    }

    // TODO Implement transaction
    public Mono<Account> saveAccount(@Nonnull final CustomerDTO customer, @Nonnull final BigDecimal initialCredit) {
        final Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .customerId(customer.getId())
                .build();
        return accountRepository.save(account);
    }

    private AccountDTO mapToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .build();
    }

    @Override
    public Mono<AccountDTO> getAccountByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId)
                .switchIfEmpty(Mono.error(new AccountNotFoundException()))
                .map(this::mapToAccountDTO);
    }
}
