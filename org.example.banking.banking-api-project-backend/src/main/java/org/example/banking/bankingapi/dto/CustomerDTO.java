package org.example.banking.bankingapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.example.banking.bankingapi.models.Account;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class CustomerDTO {

    private final String id;
    private final String name;
    private final String surname;
    private List<AccountDTO> accounts;

    @JsonIgnore
    private List<String> accountsIds;

    public List<String> getAccountsIds() {
        if (this.accountsIds == null) {
            this.accountsIds = new ArrayList<>();
        }
        return this.accountsIds;
    }

    public List<AccountDTO> getAccounts() {
       if (this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        return this.accounts;
    }

}
