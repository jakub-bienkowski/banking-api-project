package org.example.banking.bankingapi.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Customer {

    private final String id;
    private final String name;
    private final String surname;
    private final List<String> accountsIds;

}
