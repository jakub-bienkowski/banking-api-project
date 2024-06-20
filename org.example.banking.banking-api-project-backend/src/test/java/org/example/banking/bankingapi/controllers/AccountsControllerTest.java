package org.example.banking.bankingapi.controllers;

import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.requests.AccountCreationRequest;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.example.banking.bankingapi.services.banking.BankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "configType=inMemory")
@AutoConfigureWebTestClient
public class AccountsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BankingService bankingService;

    @Test
    public void testCreateAccount() {
        AccountCreationRequest request = new AccountCreationRequest("test-customer-id", new BigDecimal("1000"));


        when(bankingService.createAccount(any(AccountCreationRequest.class)))
                .thenReturn(Mono.just(AccountDTO.builder().id("1").build()));

        webTestClient.post()
                .uri("api/accounts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    public void testBadRequest() {
        AccountCreationRequest request = new AccountCreationRequest("test-customer-id", new BigDecimal("1000"));


        when(bankingService.createAccount(any(AccountCreationRequest.class)))
                .thenThrow(new CustomerNotFoundException());

        webTestClient.post()
                .uri("/accounts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }
}