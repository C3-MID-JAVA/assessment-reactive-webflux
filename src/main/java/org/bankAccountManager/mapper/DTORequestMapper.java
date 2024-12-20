package org.bankAccountManager.mapper;

import org.bankAccountManager.DTO.request.*;
import org.bankAccountManager.entity.*;
import org.bankAccountManager.util.function.TransactionMappers;
import org.springframework.beans.BeanUtils;
import reactor.core.publisher.Mono;

public class DTORequestMapper {
    public static Mono<AccountRequestDTO> toAccountRequestDTO(Mono<Account> a) {
        return a.map(aEnt -> {
            AccountRequestDTO aDTO = new AccountRequestDTO();
            BeanUtils.copyProperties(aEnt, aDTO);
            return aDTO;
        });
    }

    public static Mono<BranchRequestDTO> toBranchRequestDTO(Mono<Branch> b) {
        return b.map(bEnt -> {
            BranchRequestDTO bDTO = new BranchRequestDTO();
            BeanUtils.copyProperties(bEnt, bDTO);
            return bDTO;
        });
    }

    public static Mono<CardRequestDTO> toCardRequestDTO(Mono<Card> c) {
        return c.map(cEnt -> {
            CardRequestDTO cDTO = new CardRequestDTO();
            BeanUtils.copyProperties(cEnt, cDTO);
            return cDTO;
        });
    }

    public static Mono<CustomerRequestDTO> toCustomerRequestDTO(Mono<Customer> c) {
        return c.map(cEnt -> {
            CustomerRequestDTO cDTO = new CustomerRequestDTO();
            BeanUtils.copyProperties(cEnt, cDTO);
            return cDTO;
        });
    }

    public static Mono<TransactionRequestDTO> toTransactionRequestDTO(Mono<Transaction> t) {
        return t.map(tEnt -> {
            TransactionRequestDTO tDTO = new TransactionRequestDTO();
            BeanUtils.copyProperties(tEnt, tDTO);
            return tDTO;
        });
    }

    public static Mono<Account> toAccount(Mono<AccountRequestDTO> aDTO) {
        return aDTO.map(dto -> {
            Account a = new Account();
            BeanUtils.copyProperties(dto, a);
            return a;
        });
    }

    public static Mono<Branch> toBranch(Mono<BranchRequestDTO> bDTO) {
        return bDTO.map(dto -> {
            Branch b = new Branch();
            BeanUtils.copyProperties(dto, b);
            return b;
        });
    }

    public static Mono<Card> toCard(Mono<CardRequestDTO> cDTO) {
        return cDTO.map(dto -> {
            Card c = new Card();
            BeanUtils.copyProperties(dto, c);
            return c;
        });
    }

    public static Mono<Customer> toCustomer(Mono<CustomerRequestDTO> cDTO) {
        return cDTO.map(dto -> {
            Customer c = new Customer();
            BeanUtils.copyProperties(dto, c);
            return c;
        });
    }

    public static Mono<Transaction> toTransaction(Mono<TransactionRequestDTO> tDTO) {
        return tDTO.map(TransactionMappers.toTransaction);
    }
}
