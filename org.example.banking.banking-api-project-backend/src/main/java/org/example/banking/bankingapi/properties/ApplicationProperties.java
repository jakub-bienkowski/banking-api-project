package org.example.banking.bankingapi.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "banking-api")
public class ApplicationProperties {

    public ApplicationProperties() {
    }
}
