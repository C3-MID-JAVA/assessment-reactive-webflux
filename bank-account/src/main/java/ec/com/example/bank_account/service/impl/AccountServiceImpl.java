package ec.com.example.bank_account.service.impl;

import ec.com.example.bank_account.dto.AccountRequestDTO;
import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.AccountMapper;
import ec.com.example.bank_account.repository.AccountRepository;
import ec.com.example.bank_account.service.AccountService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Mono<AccountResponseDTO> createAccount(AccountRequestDTO accountRequestDTO) {
        return Mono.just(accountRequestDTO)
                .map(accountMapper::mapToEntity)
                .flatMap(accountRepository::save)
                .map(accountMapper::mapToDTO);

    }

    @Override
    public Flux<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll()
                .map(accountMapper::mapToDTO)
                .switchIfEmpty(Flux.error(new EmptyCollectionException("No accounts records found.")));
    }

}