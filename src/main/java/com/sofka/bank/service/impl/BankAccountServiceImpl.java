package com.sofka.bank.service.impl;

import com.sofka.bank.dto.BankAccountDTO;
import com.sofka.bank.entity.BankAccount;
import com.sofka.bank.mapper.DTOMapper;
import com.sofka.bank.repository.BankAccountRepository;
import com.sofka.bank.service.BankAccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Mono<BankAccountDTO> createAccount(BankAccountDTO bankAccountDTO) {

        if (bankAccountDTO.getAccountHolder() == null || bankAccountDTO.getAccountHolder().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Account holder is required"));
        }
        if (bankAccountDTO.getGlobalBalance() == null || bankAccountDTO.getGlobalBalance() < 0) {
            return Mono.error(new IllegalArgumentException("Global balance must be a positive number"));
        }

        return isAccountNumberUnique(bankAccountDTO.getAccountNumber())
                .flatMap(isUnique -> {
                    if (isUnique) {
                        return Mono.error(new IllegalArgumentException("Account number already exists"));
                    }

                    Supplier<BankAccount> createBankAccount = () -> {
                        BankAccount bankAccount = new BankAccount();
                        bankAccount.setAccountHolder(bankAccountDTO.getAccountHolder());
                        bankAccount.setGlobalBalance(bankAccountDTO.getGlobalBalance());
                        bankAccount.setAccountNumber(bankAccountDTO.getAccountNumber());
                        return bankAccount;
                    };

                    return bankAccountRepository.save(createBankAccount.get())
                            .map(DTOMapper::toBankAccountDTO);
                });
    }

    @Override
    public Flux<BankAccountDTO> getAllAccounts() {
        return bankAccountRepository.findAll()
                .map(DTOMapper::toBankAccountDTO);

    }

    @Override
    public Mono<Boolean> isAccountNumberUnique(String accountNumber) {
        return bankAccountRepository.existsByAccountNumber(accountNumber);
    }
}