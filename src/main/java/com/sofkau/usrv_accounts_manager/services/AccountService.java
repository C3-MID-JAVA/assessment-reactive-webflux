package com.sofkau.usrv_accounts_manager.services;

import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<AccountDTO> createAccount(AccountDTO account);
    Flux<AccountDTO> getAllAccounts();
}
