package org.example.banking.bankingapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AccountDTO {

    private String id;
    private String customerId;
    private BigDecimal balance;

}
