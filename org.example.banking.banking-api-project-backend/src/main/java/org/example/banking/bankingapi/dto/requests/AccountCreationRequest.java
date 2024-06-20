package org.example.banking.bankingapi.dto.requests;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AccountCreationRequest {

    @Nonnull
    private final String customerId;

    @Nullable
    private final BigDecimal initialCredit;
}
