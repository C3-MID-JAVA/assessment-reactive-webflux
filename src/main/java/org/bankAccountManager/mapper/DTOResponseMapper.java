package org.bankAccountManager.mapper;

import org.bankAccountManager.DTO.response.*;
import org.bankAccountManager.entity.*;
import org.springframework.beans.BeanUtils;
import reactor.core.publisher.Mono;

public class DTOResponseMapper {
    public static Mono<AccountResponseDTO> toAccountResponseDTO(Mono<Account> a) {
        return a.map(aEnt -> {
            AccountResponseDTO aDTO = new AccountResponseDTO();
            BeanUtils.copyProperties(aEnt, aDTO);
            return aDTO;
        });
    }

    public static Mono<BranchResponseDTO> toBranchResponseDTO(Mono<Branch> b) {
        return b.map(bEnt -> {
            BranchResponseDTO bDTO = new BranchResponseDTO();
            BeanUtils.copyProperties(bEnt, bDTO);
            return bDTO;
        });
    }

    public static Mono<CardResponseDTO> toCardResponseDTO(Mono<Card> c) {
        return c.map(cEnt -> {
            CardResponseDTO cDTO = new CardResponseDTO();
            BeanUtils.copyProperties(cEnt, cDTO);
            return cDTO;
        });
    }

    public static Mono<CustomerResponseDTO> toCustomerResponseDTO(Mono<Customer> c) {
        return c.map(cEnt -> {
            CustomerResponseDTO cDTO = new CustomerResponseDTO();
            BeanUtils.copyProperties(cEnt, cDTO);
            return cDTO;
        });
    }

    public static Mono<TransactionResponseDTO> toTransactionResponseDTO(Mono<Transaction> t) {
        return t.map(tEnt -> {
            TransactionResponseDTO tDTO = new TransactionResponseDTO();
            BeanUtils.copyProperties(tEnt, tDTO);
            return tDTO;
        });
    }

    public static Mono<Account> toAccount(Mono<AccountResponseDTO> aDTO) {
        return aDTO.map(dto -> {
            Account a = new Account();
            BeanUtils.copyProperties(dto, a);
            return a;
        });
    }

    public static Mono<Branch> toBranch(Mono<BranchResponseDTO> bDTO) {
        return bDTO.map(dto -> {
            Branch b = new Branch();
            BeanUtils.copyProperties(dto, b);
            return b;
        });
    }

    public static Mono<Card> toCard(Mono<CardResponseDTO> cDTO) {
        return cDTO.map(dto -> {
            Card c = new Card();
            BeanUtils.copyProperties(dto, c);
            return c;
        });
    }

    public static Mono<Customer> toCustomer(Mono<CustomerResponseDTO> cDTO) {
        return cDTO.map(dto -> {
            Customer c = new Customer();
            BeanUtils.copyProperties(dto, c);
            return c;
        });
    }

    public static Mono<Transaction> toTransaction(Mono<TransactionResponseDTO> tDTO) {
        return tDTO.map(dto -> {
            Transaction t = new Transaction();
            BeanUtils.copyProperties(dto, t);
            return t;
        });
    }
}
