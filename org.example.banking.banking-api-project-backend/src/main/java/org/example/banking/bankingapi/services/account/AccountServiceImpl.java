package org.example.banking.bankingapi.services.account;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.dto.TransactionDTO;
import org.example.banking.bankingapi.dto.requests.AddAccountRequest;
import org.example.banking.bankingapi.exceptions.AccountNotFoundException;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.models.Account;
import org.example.banking.bankingapi.repositories.account.AccountRepository;
import org.example.banking.bankingapi.services.customer.CustomerService;
import org.example.banking.bankingapi.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    @Autowired
    public AccountServiceImpl(@Nonnull final AccountRepository accountRepository,
                              @Nonnull final CustomerService customerService,
                              @Nonnull final TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    @Override
    public Mono<AccountDTO> createAccount(@Nonnull final AddAccountRequest request) {
        return customerService.findById(request.getCustomerId())
                .flatMap(this::saveAccount)
                .flatMap(this::updateCustomerAccounts)
                .flatMap(account -> this.performInitialCreditTransaction(account, request.getInitialCredit()))
                .flatMap(this::enrichWithTransactions);
    }

    private Mono<Account> updateCustomerAccounts(Account account) {
        return customerService.findById(account.getCustomerId())
                .switchIfEmpty(Mono.error(new AccountNotFoundException()))
                .flatMap(customer -> {
                    customer.getAccountsIds().add(account.getId());
                    return customerService.save(customer).then(Mono.just(account));
                });
    }

    private Mono<AccountDTO> enrichWithTransactions(@Nonnull final Account account) {
        Mono<AccountDTO> accountMono = Mono.just(this.mapToAccountDTO(account));
        Flux<TransactionDTO> transactionsFlux = transactionService.getTransactionsByAccountId(account.getId());

        return accountMono.zipWith(transactionsFlux.collectList(), (accountDTO, transactions) -> {
            accountDTO.getTransactions().addAll(transactions);
            return accountDTO;
        });
    }

    public Mono<Account> saveAccount(@Nonnull final CustomerDTO customer) {
        final Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .customerId(customer.getId())
                .balance(BigDecimal.ZERO)
                .transactionsIds(new ArrayList<>())
                .build();

        return accountRepository.save(account);
    }

    @Override
    public Flux<AccountDTO> getAccountsByCustomerId(@Nonnull final String customerId) {
        return customerService.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .flatMapIterable(CustomerDTO::getAccountsIds)
                .flatMap(accountRepository::findById)
                .flatMap(this::enrichWithTransactions);
    }

    private Mono<Account> performInitialCreditTransaction(@Nonnull final Account account, @Nullable final BigDecimal initialCredit) {
        if (initialCredit == null || initialCredit.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.just(account);
        }

        final TransactionDTO transaction = TransactionDTO.builder()
                .accountId(account.getId())
                .amount(initialCredit)
                .build();

        return transactionService.performTransaction(transaction)
                .flatMap(this::updateAccountTransactions);
    }

    @Override
    public Mono<Account> updateAccountTransactions(TransactionDTO transaction) {
        return accountRepository.findById(transaction.getAccountId())
                .switchIfEmpty(Mono.error(new AccountNotFoundException()))
                .flatMap(account -> {
                    account.getTransactionsIds().add(transaction.getId());
                    account.updateBalance(transaction.getAmount());
                    return accountRepository.save(account);
                });
    }

    private AccountDTO mapToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .transactions(new ArrayList<>())
                .build();
    }

}
