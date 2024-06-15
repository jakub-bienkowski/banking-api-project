package org.example.banking.bankingapi.config;

import jakarta.annotation.Nonnull;
import org.example.banking.bankingapi.models.Customer;
import org.example.banking.bankingapi.properties.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanConfig {

    @Bean
    @ConditionalOnProperty(value = "banking-api.memoryBasedConfig", matchIfMissing = true)
    public List<Customer> initialCustomers(@Nonnull ApplicationProperties properties) {
        return properties.getInitialCustomers();
    }
}
