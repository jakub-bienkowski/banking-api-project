package org.example.banking.bankingapi.services.banking;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.dto.TransactionDTO;
import org.example.banking.bankingapi.dto.requests.AccountCreationRequest;
import org.example.banking.bankingapi.exceptions.AccountNotFoundException;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.services.account.AccountService;
import org.example.banking.bankingapi.services.customer.CustomerService;
import org.example.banking.bankingapi.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
public class BankingServiceImpl implements BankingService {

    private final AccountService accountService;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    @Autowired
    public BankingServiceImpl(@Nonnull final AccountService accountService,
                              @Nonnull final CustomerService customerService,
                              @Nonnull final TransactionService transactionService) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    @Override
    public Mono<CustomerDTO> getCustomerDataById(String customerId) {
        log.debug("Fetching customer data by id: {}", customerId);

        return customerService.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()))
                .flatMap(this::enrichWithAccounts);
    }

    private Mono<CustomerDTO> enrichWithAccounts(CustomerDTO customer) {
        return Flux.fromIterable(customer.getAccountsIds())
                .flatMap(accountService::findById)
                .flatMap(this::enrichWithTransactions)
                .collectList()
                .map(accounts -> {
                    customer.setAccounts(accounts);
                    return customer;
                });
    }

    private Mono<AccountDTO> enrichWithTransactions(AccountDTO account) {
        log.debug("Fetching transactions for account: {}", account.getId());

        return Flux.fromIterable(account.getTransactionsIds())
                .flatMap(transactionService::findById)
                .collectList()
                .map(transactions -> {
                    account.setTransactions(transactions);
                    return account;
                });
    }

    @Override
    public Mono<AccountDTO> createAccount(@Nonnull final AccountCreationRequest request) {
        return findCustomer(request.getCustomerId())
                .flatMap(this::createAccount)
                .flatMap(account -> {
                    if (hasInitialCredit(request.getInitialCredit())) {
                        return addTransaction(account, request.getInitialCredit());
                    } else {
                        return Mono.just(account);
                    }
                })
                .flatMap(this::enrichWithTransactions);
    }

    private Mono<AccountDTO> createAccount(CustomerDTO customer) {
        final AccountDTO accountDTO = buildAccountDTO(customer);

        return accountService.save(accountDTO)
                .flatMap(savedAccount -> updateCustomer(customer, savedAccount));
    }

    private Mono<AccountDTO> updateCustomer(CustomerDTO customer, AccountDTO savedAccount) {
        customer.getAccountsIds().add(savedAccount.getId());
        return customerService.save(customer)
                .then(Mono.just(savedAccount));
    }

    private Mono<AccountDTO> addTransaction(AccountDTO account, BigDecimal initialCredit) {
        log.info("Adding transaction for account: {}. Amount: {} ", account.getId(), initialCredit);

        final TransactionDTO transaction = buildTransactionDTO(account, initialCredit);

        return transactionService.save(transaction)
                .flatMap(this::updateAccountTransactions);
    }

    private Mono<CustomerDTO> findCustomer(String customerId) {
        return customerService.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException()));
    }

    public Mono<AccountDTO> updateAccountTransactions(TransactionDTO transaction) {
        return accountService.findById(transaction.getAccountId())
                .switchIfEmpty(Mono.error(new AccountNotFoundException()))
                .flatMap(account -> {
                    account.getTransactionsIds().add(transaction.getId());
                    account.updateBalance(transaction.getAmount());
                    return accountService.save(account);
                });
    }

    private boolean hasInitialCredit(BigDecimal initialCredit) {
        return initialCredit != null && initialCredit.compareTo(BigDecimal.ZERO) > 0;
    }

    private AccountDTO buildAccountDTO(CustomerDTO customer) {
        return AccountDTO.builder()
                .customerId(customer.getId())
                .balance(BigDecimal.ZERO)
                .build();
    }

    private TransactionDTO buildTransactionDTO(AccountDTO account, BigDecimal initialCredit) {
        return TransactionDTO.builder()
                .accountId(account.getId())
                .amount(initialCredit)
                .build();
    }
}