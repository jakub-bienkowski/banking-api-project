package org.example.banking.bankingapi.controllers;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.AccountDTO;


import org.example.banking.bankingapi.dto.requests.AddAccountRequest;
import org.example.banking.bankingapi.services.account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountService accountService;

    public AccountsController(@Nonnull final AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountDTO>> createAccount(@RequestBody @Validated AddAccountRequest request) {
        return accountService.createAccount(request)
                .map(account -> new ResponseEntity<>(account, HttpStatus.CREATED));
    }
}