package org.example.banking.bankingapi.controllers;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.dto.CustomerDTO;
import org.example.banking.bankingapi.services.banking.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    private final BankingService bankingService;

    @Autowired
    public CustomersController(@Nonnull final BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<CustomerDTO>> getCustomers() {
        return ResponseEntity.ok(bankingService.getAllCustomers());
    }

    @GetMapping(path = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CustomerDTO>> getCustomer(@PathVariable String customerId) {
        return bankingService.getCustomerDataById(customerId)
                .map(ResponseEntity::ok);
    }
}
