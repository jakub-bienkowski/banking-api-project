package org.example.banking.bankingapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CustomerDTO {

    private final String id;
    private final String name;
    private final String surname;
    private final List<String> accountsIds;

}
