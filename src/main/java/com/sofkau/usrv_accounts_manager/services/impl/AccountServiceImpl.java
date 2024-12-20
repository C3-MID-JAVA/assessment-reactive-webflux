package com.sofkau.usrv_accounts_manager.services.impl;

import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.mapper.DTOMapper;
import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.repository.AccountRepository;
import com.sofkau.usrv_accounts_manager.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private  final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
        return accountRepository
                .findByAccountNumber(accountDTO.getAccountNumber())
                .hasElement()
                .flatMap(hasElement -> {
                    if (hasElement) {
                        return Mono.error(new RuntimeException("Account already exists"));
                    }
                    return accountRepository.save(DTOMapper.toAccount(accountDTO))
                            .map(DTOMapper::toAccountDTO)
                            .switchIfEmpty(Mono.error(new RuntimeException("Account could not be saved")));
                });
    }

    @Override
    public Flux<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().map(DTOMapper::toAccountDTO);
    }
}
