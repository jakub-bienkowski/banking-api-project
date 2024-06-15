package org.example.banking.bankingapi.controllers;


import org.example.banking.bankingapi.exceptions.AccountNotFoundException;
import org.example.banking.bankingapi.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = AccountsController.class)
public class AccountsControllerAdvice {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(CustomerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given id not found");
    }


    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
