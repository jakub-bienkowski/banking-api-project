package org.example.banking.bankingapi.controllers;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.services.banking.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class CustomersController {

    private final BankingService bankingService;

    @Autowired
    public CustomersController(@Nonnull final BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CustomerDTO>> getUser(@PathVariable String userId) {
        return bankingService.getCustomerDataById(userId)
                .map(ResponseEntity::ok);
    }
}
