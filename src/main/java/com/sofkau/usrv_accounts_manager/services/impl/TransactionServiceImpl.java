package com.sofkau.usrv_accounts_manager.services.impl;

import com.sofkau.usrv_accounts_manager.dto.TransactionDTO;
import com.sofkau.usrv_accounts_manager.mapper.DTOMapper;
import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.model.CardModel;
import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import com.sofkau.usrv_accounts_manager.model.classes.AccountDeposit;
import com.sofkau.usrv_accounts_manager.repository.AccountRepository;
import com.sofkau.usrv_accounts_manager.repository.CardRepository;
import com.sofkau.usrv_accounts_manager.repository.TransactionRepository;
import com.sofkau.usrv_accounts_manager.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository, CardRepository cardRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        TransactionModel transactionModel = DTOMapper.toTransactionModel(transactionDTO);


        Function<TransactionDTO, Mono<AccountModel>> senderAccountFetcher = dto ->
                accountRepository.findByAccountNumber(dto.getAccount().getAccountNumber())
                        .switchIfEmpty(Mono.error(new RuntimeException("Sending account not found")));


        Function<TransactionDTO, Mono<AccountModel>> processDepositTransaction = transactiondto ->
                accountRepository.findByAccountNumber(transactiondto.getAccountReceiver().getAccountNumber())
                        .switchIfEmpty(Mono.error(new RuntimeException("Receiving account not found")))
                        .flatMap(receiverAcc -> senderAccountFetcher.apply(transactiondto)
                                .flatMap(senderAcc -> {
                                    transactionModel.processTransaction();
                                    receiverAcc.setBalance(receiverAcc.getBalance().add(transactionModel.getAmount()));
                                    senderAcc.setBalance(senderAcc.getBalance().subtract(transactionModel.getAmount()).subtract(transactionModel.getTransactionFee()));
                                    ((AccountDeposit) transactionModel).setAccountReceiver(receiverAcc);
                                    return accountRepository.save(receiverAcc)
                                            .then(Mono.just(senderAcc));
                                }));


        Function<TransactionDTO, Mono<AccountModel>> processCardTransaction = dto ->
                cardRepository.findByCardNumber(dto.getCard().getCardNumber())
                        .switchIfEmpty(Mono.error(new RuntimeException("Card not found")))
                        .flatMap(cardModel -> senderAccountFetcher.apply(transactionDTO)
                                .flatMap(senderAC -> {
                                    transactionModel.processTransaction();
                                    senderAC.setBalance(senderAC.getBalance().subtract(transactionModel.getAmount()).subtract(transactionModel.getTransactionFee()));
                                    transactionModel.setCard(cardModel);
                                    return Mono.just(senderAC);
                                }));


        if (transactionModel instanceof AccountDeposit) {
            return processDepositTransaction.apply(transactionDTO)
                    .flatMap(sender -> {
                        transactionModel.setTimestamp(LocalDateTime.now());
                        transactionModel.setAccount(sender);
                        return accountRepository.save(sender)
                                .then(transactionRepository.save(transactionModel));
                    })
                    .map(DTOMapper::toTransactionDTO);
        }

        return processCardTransaction.apply(transactionDTO)
                .flatMap(sender -> {
                    transactionModel.setTimestamp(LocalDateTime.now());
                    transactionModel.setAccount(sender);
                    return accountRepository.save(sender)
                            .then(transactionRepository.save(transactionModel));
                })
                .map(DTOMapper::toTransactionDTO);


    }


}
