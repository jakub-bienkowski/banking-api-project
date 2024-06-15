package org.example.banking.bankingapi.controllers;

import org.example.banking.bankingapi.dto.AccountDTO;
import org.example.banking.bankingapi.dto.requests.AddAccountRequest;
import org.example.banking.bankingapi.services.account.AccountService;
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

@SpringBootTest
@AutoConfigureWebTestClient
public class AccountsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @Test
    public void testCreateAccount() {
        AddAccountRequest request = new AddAccountRequest("test-customer-id", new BigDecimal("1000"));

        when(accountService.createAccount(any(AddAccountRequest.class)))
                .thenReturn(Mono.just(AccountDTO.builder().id("1").build()));

        webTestClient.post()
                .uri("/accounts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }
}