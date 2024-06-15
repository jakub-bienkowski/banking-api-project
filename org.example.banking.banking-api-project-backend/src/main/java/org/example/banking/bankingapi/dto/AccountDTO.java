package org.example.banking.bankingapi.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.banking.bankingapi.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class AccountDTO {

    private final String id;
    private final String customerId;
    private final BigDecimal balance;
    private final List<String> transactionsIds;

}
