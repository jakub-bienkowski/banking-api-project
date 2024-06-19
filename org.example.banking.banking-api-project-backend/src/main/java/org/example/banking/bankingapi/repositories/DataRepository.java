package org.example.banking.bankingapi.repositories;

import jakarta.annotation.Nonnull;
import reactor.core.publisher.Mono;

public interface DataRepository<T, ID> {

    Mono<T> save(@Nonnull final T t);
    Mono<T> findById(ID id);

}
