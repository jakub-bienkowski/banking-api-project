package org.example.banking.bankingapi.models;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Account {

    private final String id;
    private final String customerId;
    private BigDecimal balance;
    private List<String> transactionsIds;

    public void updateBalance(@Nonnull final BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public List<String> getTransactionsIds() {
        if (this.transactionsIds == null) {
            this.transactionsIds = new ArrayList<>();
        }
        return this.transactionsIds;
    }
}
