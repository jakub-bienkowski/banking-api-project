package org.example.banking.bankingapi.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class Account {

    private String id;
    private String customerId;
    private BigDecimal balance;

}
