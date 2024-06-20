package org.example.banking.bankingapi.controllers;

import org.example.banking.bankingapi.dto.CustomerDTO;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "configType=inMemory")
@AutoConfigureWebTestClient
public class CustomersControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BankingService bankingService;

    @Test
    public void testGetCustomer() {
        when(bankingService.getCustomerDataById(anyString()))
                .thenReturn(Mono.just(CustomerDTO.builder().id("test-user-id").build()));

        webTestClient.get()
                .uri("/api/customers/test-user-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    public void testBadRequest() {
        when(bankingService.getCustomerDataById(anyString()))
                .thenThrow(new CustomerNotFoundException());

        webTestClient.get()
                .uri("/api/customers/test-user-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}