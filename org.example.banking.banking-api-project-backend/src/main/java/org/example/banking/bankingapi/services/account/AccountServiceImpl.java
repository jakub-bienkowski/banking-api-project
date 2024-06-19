package org.example.banking.bankingapi.services.account;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.exceptions.AccountNotFoundException;
import org.example.banking.bankingapi.models.Account;
import org.example.banking.bankingapi.repositories.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(@Nonnull final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<AccountDTO> findById(String accountId) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new AccountNotFoundException()))
                .map(this::mapToDto);
    }

    @Override
    public Mono<AccountDTO> save(@Nonnull final AccountDTO account) {
        return accountRepository.save(this.mapToModel(account))
                .map(this::mapToDto);
    }

    private Account mapToModel(AccountDTO account) {
        return Account.builder()
                .id(account.getId() == null ? UUID.randomUUID().toString() : account.getId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .transactionsIds(account.getTransactionsIds())
                .build();
    }

    private AccountDTO mapToDto(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .transactionsIds(account.getTransactionsIds())
                .transactions(new ArrayList<>())
                .build();
    }

}
