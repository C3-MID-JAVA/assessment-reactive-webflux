package com.sofka.bank.service;

import com.sofka.bank.dto.BankAccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {
    Mono<BankAccountDTO> createAccount(BankAccountDTO bankAccountDTO);
    Flux<BankAccountDTO> getAllAccounts ();
    Mono<Boolean> isAccountNumberUnique(String accountNumber);
}
