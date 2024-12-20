package com.sofka.bank.mapper;

import com.sofka.bank.dto.BankAccountDTO;
import com.sofka.bank.entity.BankAccount;
import com.sofka.bank.dto.TransactionDTO;
import com.sofka.bank.entity.Transaction;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DTOMapper {
    public static BankAccountDTO toBankAccountDTO(BankAccount a){
        return new BankAccountDTO(
                a.getId(),
                a.getAccountNumber(),
                a.getAccountHolder(),
                a.getGlobalBalance(),
                new ArrayList<TransactionDTO>());
    }

    public static BankAccount toBankAccount(BankAccountDTO aDTO){
        return new BankAccount(
                aDTO.getId(),
                aDTO.getAccountNumber(),
                aDTO.getAccountHolder(),
                aDTO.getGlobalBalance(),
                aDTO.getTransactions().stream().map(DTOMapper::toTransaction).collect(Collectors.toList()));
    }



    public static TransactionDTO toTransactionDTO(Transaction t) {
        return new TransactionDTO(
                t.getId(),
                t.getTransactionType(),
                t.getAmount(),
                t.getFee(),
                t.getDate(),
                t.getDescription(),
                null
        );

    }

    public static Transaction toTransaction(TransactionDTO tDTO){
        return new Transaction(
                tDTO.getId(),
                tDTO.getTransactionType(),
                tDTO.getAmount(),
                tDTO.getFee(),
                tDTO.getDate(),
                tDTO.getDescription(),
                null

        );



    }

}
