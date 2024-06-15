package org.example.banking.bankingapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionDTO {

    private final String id;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;

    @JsonIgnore
    private final String accountId;

}
