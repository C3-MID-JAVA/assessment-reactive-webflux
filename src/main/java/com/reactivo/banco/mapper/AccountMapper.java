package com.reactivo.banco.mapper;

import com.reactivo.banco.model.dto.AccountInDTO;
import com.reactivo.banco.model.dto.AccountOutDTO;
import com.reactivo.banco.model.entity.Account;

public class AccountMapper {

    public static Account toEntity(AccountInDTO accountInDTO) {
        if (accountInDTO == null) {
            return null;
        }

        Account account = new Account();
        account.setAccountNumber(accountInDTO.getAccountNumber());
        account.setBalance(accountInDTO.getBalance());
        account.setCustumerId(accountInDTO.getCustumerId());
        account.setCardId(accountInDTO.getCardId());

        return account;
    }

    public static AccountOutDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountOutDTO accountOutDTO = new AccountOutDTO();
        accountOutDTO.setId(account.getId());
        accountOutDTO.setAccountNumber(account.getAccountNumber());
        accountOutDTO.setBalance(account.getBalance());
        accountOutDTO.setCustumerId(account.getCustumerId());
        accountOutDTO.setCardId(account.getCardId());

        return accountOutDTO;
    }

}
