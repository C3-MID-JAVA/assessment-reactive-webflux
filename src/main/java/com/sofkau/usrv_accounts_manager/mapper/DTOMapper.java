package com.sofkau.usrv_accounts_manager.mapper;

import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.dto.CardDTO;
import com.sofkau.usrv_accounts_manager.dto.TransactionDTO;
import com.sofkau.usrv_accounts_manager.model.*;
import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import com.sofkau.usrv_accounts_manager.model.classes.*;


import java.util.ArrayList;
import java.util.stream.Collectors;

public class DTOMapper {
    public static AccountDTO toAccountDTO(AccountModel account) {
        if (account == null) {  return null; }

        return new AccountDTO(
                account.getTransactions() != null ? account.getTransactions().stream()
                .map(DTOMapper::toTransactionDTO).collect(Collectors.toList()) : new ArrayList<>(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getOwnerName(),
                account.getCards() != null ? account.getCards().stream()
                        .map(DTOMapper::toCardDTO).collect(Collectors.toList()) : new ArrayList<>());
    }

    public static AccountModel toAccount(AccountDTO accountDTO) {
        return  new AccountModel(
                null,
                accountDTO.getAccountNumber(),
                accountDTO.getAccountBalance(),
                accountDTO.getAccountOwner(),
                accountDTO.getAccountType(),
                accountDTO.getCards() != null ? accountDTO.getCards().stream()
                        .map(DTOMapper::toCardModel).collect(Collectors.toList()) : new ArrayList<>(),
                accountDTO.getTransactions() != null ? accountDTO.getTransactions().stream()
                        .map(DTOMapper::toTransactionModel).collect(Collectors.toList()) : new ArrayList<>()
        );
    }

    public static TransactionDTO toTransactionDTO(TransactionModel transaction) {
        TransactionDTO transactionDto = new TransactionDTO(transaction.getDescription(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionFee(),
                null,
                null);
        if(transaction instanceof PaymentStoreTransaction paymentStoreTransaction) {
            transactionDto.setMarketName(paymentStoreTransaction.getMarketName());
        }

        if(transaction instanceof PaymentWebTransaction paymentStoreTransaction) {
            transactionDto.setWebsite(paymentStoreTransaction.getWebsite());
        }

        if(transaction instanceof AtmTransaction atmTransaction) {
            transactionDto.setAtmName(atmTransaction.getAtmName());
            transactionDto.setOperationType(atmTransaction.getOperationType());
        }
        if(transaction instanceof BranchDeposit branchTransaction) {
            transactionDto.setBranchName(branchTransaction.getBranchName());
        }
        if(transaction instanceof AccountDeposit accountDeposit) {
            transactionDto.setAccountReceiver(accountDeposit.getAccountReceiver());
        }
        return transactionDto;
    }
    public static TransactionModel toTransactionModel(TransactionDTO transactionDTO) {
        TransactionModel transaction = null;
        switch (transactionDTO.getTransactionType()) {
            case ConstansTrType.STORE_PURCHASE:
                transaction  = new PaymentStoreTransaction();
                ((PaymentStoreTransaction)  transaction).setMarketName(transactionDTO.getMarketName());
                break;
            case ConstansTrType.WEB_PURCHASE:
                transaction  = new PaymentWebTransaction();
                ((PaymentWebTransaction)  transaction).setWebsite(transactionDTO.getWebsite());
                break;
            case ConstansTrType.ATM:
                transaction  = new AtmTransaction();
                ((AtmTransaction) transaction).setAtmName(transactionDTO.getAtmName());
                ((AtmTransaction) transaction).setOperationType(transactionDTO.getOperationType());
                break;
            case ConstansTrType.BRANCH_DEPOSIT:
                transaction  = new BranchDeposit();
                ((BranchDeposit) transaction).setBranchName(transactionDTO.getBranchName());
                break;
            case ConstansTrType.BETWEEN_ACCOUNT:
                transaction  = new AccountDeposit();
                ((AccountDeposit) transaction).setAccountReceiver(transactionDTO.getAccountReceiver());
                break;
        }
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionFee(transactionDTO.getTransactionFee());

        return transaction;

    }

    public static CardDTO  toCardDTO(CardModel card) {
        return new CardDTO(card.getCardName(),
                card.getCardNumber(),
                card.getCardType(),
                card.getCardStatus(),
                card.getCardExpiryDate(),
                card.getCardLimit(),
                card.getCardHolderName(),
                null,
                card.getTransactions() != null?
                card.getTransactions().stream()
                        .map(DTOMapper::toTransactionDTO).collect(Collectors.toList()) : new ArrayList<>());
    }

    public static CardModel toCardModel(CardDTO cardDTO) {
        return new CardModel(null,
                cardDTO.getCardName(),
                cardDTO.getCardNumber(),
                cardDTO.getCardType(),
                cardDTO.getCardStatus(),
                cardDTO.getCardExpiryDate(),
                null,
                cardDTO.getCardLimit(),
                cardDTO.getCardHolderName(),
                null,
                cardDTO.getTransactions() != null ?
                        cardDTO.getTransactions().stream()
                                .map(DTOMapper::toTransactionModel).collect(Collectors.toList()) : new ArrayList<>());

    }
}
