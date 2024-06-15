package org.example.banking.bankingapi.properties;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.example.banking.bankingapi.models.Customer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@ConfigurationProperties(prefix = "banking-api")
public class ApplicationProperties {

    private final List<Customer> initialCustomers;

    public ApplicationProperties(@Nullable final List<Customer> initialCustomers) {
        this.initialCustomers = Optional.ofNullable(initialCustomers).orElse(new ArrayList<>());
    }
}
