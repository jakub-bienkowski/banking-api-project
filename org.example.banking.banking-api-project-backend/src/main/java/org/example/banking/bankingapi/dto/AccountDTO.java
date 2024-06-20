package org.example.banking.bankingapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class AccountDTO {

    private final String id;
    private final String customerId;
    private BigDecimal balance;

    @Setter
    private List<TransactionDTO> transactions;

    @JsonIgnore
    private List<String> transactionsIds;

    public List<TransactionDTO> getTransactions() {
        if (this.transactions == null) {
            this.transactions = new ArrayList<>();
        }
        return this.transactions;
    }

    public List<String> getTransactionsIds() {
        if (this.transactionsIds == null) {
            this.transactionsIds = new ArrayList<>();
        }
        return this.transactionsIds;
    }

    public void updateBalance(BigDecimal amount) {
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        this.balance = this.balance.add(amount);
    }
}
