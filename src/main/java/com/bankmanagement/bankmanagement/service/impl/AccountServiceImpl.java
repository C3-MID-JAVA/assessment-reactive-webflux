package com.bankmanagement.bankmanagement.service.impl;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.mapper.AccountMapper;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.model.User;
import com.bankmanagement.bankmanagement.repository.AccountRepository;
import com.bankmanagement.bankmanagement.repository.UserRepository;
import com.bankmanagement.bankmanagement.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccountResponseDTO create(AccountRequestDTO accountRequestDTO) {
        User user = userRepository.findById(accountRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0,8));
        account.setBalance(0);
        account.setUserId(user.getId());

        accountRepository.save(account);

        return AccountMapper.fromEntity(account);
    }

    @Override
    public List<AccountResponseDTO> getAllByUserId(String userId){
        return accountRepository.findByUserId(userId).stream().map(AccountMapper::fromEntity).toList();
    }

    @Override
    public AccountResponseDTO findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new NotFoundException("Account not found"));
        return AccountMapper.fromEntity(account);
    }
}
