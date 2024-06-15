package org.example.banking.bankingapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionDTO {

    private final String id;
    private final BigDecimal amount;
    private final String accountId;
    private final LocalDateTime timestamp;

}
