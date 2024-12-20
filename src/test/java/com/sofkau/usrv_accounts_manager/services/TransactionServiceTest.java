package com.sofkau.usrv_accounts_manager.services;

import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.dto.CardDTO;
import com.sofkau.usrv_accounts_manager.dto.TransactionDTO;
import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.model.CardModel;
import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import com.sofkau.usrv_accounts_manager.model.classes.AtmTransaction;
import com.sofkau.usrv_accounts_manager.model.classes.BranchDeposit;
import com.sofkau.usrv_accounts_manager.repository.AccountRepository;
import com.sofkau.usrv_accounts_manager.repository.CardRepository;
import com.sofkau.usrv_accounts_manager.repository.TransactionRepository;
import com.sofkau.usrv_accounts_manager.services.impl.AccountServiceImpl;
import com.sofkau.usrv_accounts_manager.services.impl.CardServiceImpl;
import com.sofkau.usrv_accounts_manager.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionModel transaction;
    private CardModel card;
    private AccountModel account;
    private AccountModel accountReceiver;
    private TransactionDTO transactionDTO;
    private CardDTO cardDTO;
    private AccountDTO accountDTO;
    private AccountDTO accountReceiverDTO;

    @BeforeEach
    void setUp() {
        card = new CardModel();
        account = new AccountModel();
        accountReceiver = new AccountModel();
        card.setCardNumber("123456789");
        account.setAccountNumber("123456789");
        accountReceiver.setAccountNumber("987654321");
        account.setBalance(BigDecimal.valueOf(1000));
        accountReceiver.setBalance(BigDecimal.valueOf(1000));
        cardDTO = new CardDTO();
        cardDTO.setCardNumber("123456789");
        accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        accountReceiverDTO = new AccountDTO();
        accountReceiverDTO.setAccountNumber("987654321");
        transactionDTO = new TransactionDTO("Test Transaction", BigDecimal.valueOf(10),
                "ATM", BigDecimal.valueOf(0), accountDTO, cardDTO);

    }

    @Test
    @DisplayName("Should create a new transaction when account and card exist and is type ATM with operation type ATM_DEBIT")
    void createTransaction_success() throws Exception {
        transactionDTO.setOperationType(ConstansTrType.ATM_DEBIT);
        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()))
                .thenReturn(Mono.just(account));
        when(accountRepository.save(any(AccountModel.class)))
                .thenReturn(Mono.just(account));
        when(cardRepository.findByCardNumber(cardDTO.getCardNumber()))
                .thenReturn(Mono.just(card));

        transaction = new AtmTransaction();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionFee(BigDecimal.valueOf(1));

        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.createTransaction(transactionDTO))
                .assertNext(transactionResp -> {
                    assertEquals(transactionDTO.getDescription(), transactionResp.getDescription());
                    assertEquals(BigDecimal.valueOf(1), transactionResp.getTransactionFee());
                })
                .verifyComplete();

        verify(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository).findByCardNumber(cardDTO.getCardNumber());
        verify(transactionRepository).save(any(TransactionModel.class));
    }

    @Test
    @DisplayName("Should create a new transaction when account and card exist and is type ATM with operation type ATM_DEPOSITO")
    void createTransaction_success_ATM() throws Exception {
        transactionDTO.setOperationType(ConstansTrType.ATM_DEPOSIT);
        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()))
                .thenReturn(Mono.just(account));
        when(cardRepository.findByCardNumber(cardDTO.getCardNumber()))
                .thenReturn(Mono.just(card));
        when(accountRepository.save(any(AccountModel.class)))
                .thenReturn(Mono.just(account));

        transaction = new AtmTransaction();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionFee(BigDecimal.valueOf(2));

        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.createTransaction(transactionDTO))
                .assertNext(transactionResp -> {
                    assertEquals(transactionDTO.getDescription(), transactionResp.getDescription());
                    assertEquals(BigDecimal.valueOf(2), transactionResp.getTransactionFee());
                })
                .verifyComplete();

        verify(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository).findByCardNumber(cardDTO.getCardNumber());
        verify(transactionRepository).save(any(TransactionModel.class));


    }

    @Test
    @DisplayName("Should create a new transaction when account and card exist and is type BRANCH_DEPOSIT ")
    void createTransaction_success_BRANCH_DEPOSIT() throws Exception {
        transactionDTO.setTransactionType(ConstansTrType.BRANCH_DEPOSIT);
        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()))
                .thenReturn(Mono.just(account));
        when(cardRepository.findByCardNumber(cardDTO.getCardNumber()))
                .thenReturn(Mono.just(card));
        when(accountRepository.save(any(AccountModel.class)))
                .thenReturn(Mono.just(account));

        transaction = new BranchDeposit();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionFee(BigDecimal.valueOf(0));

        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.createTransaction(transactionDTO))
                .assertNext(transactionResp -> {
                    assertEquals(transactionDTO.getDescription(), transactionResp.getDescription());
                    assertEquals(BigDecimal.valueOf(0), transactionResp.getTransactionFee());
                })
                .verifyComplete();

        verify(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository).findByCardNumber(cardDTO.getCardNumber());
        verify(transactionRepository).save(any(TransactionModel.class));

    }

    @Test
    @DisplayName("Should create a new transaction when account and card exist and is type BETWEEN_ACCOUNT ")
    void createTransaction_success_BETWEEN_ACCOUNT() throws Exception {
        transactionDTO.setTransactionType(ConstansTrType.BETWEEN_ACCOUNT);
        transactionDTO.setAccountReceiver(accountReceiver);
        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()))
                .thenReturn(Mono.just(account));
        when(accountRepository.findByAccountNumber(accountReceiverDTO.getAccountNumber()))
                .thenReturn(Mono.just(accountReceiver));
        when(accountRepository.save(any(AccountModel.class)))
                .thenReturn(Mono.just(account));
        when(accountRepository.save(any(AccountModel.class)))
                .thenReturn(Mono.just(accountReceiver));

        transaction = new BranchDeposit();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setTransactionType(transactionDTO.getTransactionType());

        transaction.setTransactionFee(BigDecimal.valueOf(1.5));

        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.createTransaction(transactionDTO))
                .assertNext(transactionResp -> {
                    assertEquals(transactionDTO.getDescription(), transactionResp.getDescription());
                    assertEquals(BigDecimal.valueOf(1.5), transactionResp.getTransactionFee());
                })
                .verifyComplete();

        verify(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository, never()).findByCardNumber(anyString());
        verify(transactionRepository).save(any(TransactionModel.class));

    }

    @Test
    @DisplayName("Should create a new transaction when account and card exist and is type STORE_PURCHASE ")
    void createTransaction_success_STORE_PURCHASE() throws Exception {
        transactionDTO.setTransactionType(ConstansTrType.STORE_PURCHASE);
        transactionDTO.setBranchName("TEST");
        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()))
                .thenReturn(Mono.just(account));
        when(cardRepository.findByCardNumber(cardDTO.getCardNumber()))
                .thenReturn(Mono.just(card));
        when(accountRepository.save(any(AccountModel.class)))
                .thenReturn(Mono.just(account));

        transaction = new BranchDeposit();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionFee(BigDecimal.valueOf(0));

        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.createTransaction(transactionDTO))
                .assertNext(transactionResp -> {
                    assertEquals(transactionDTO.getDescription(), transactionResp.getDescription());
                    assertEquals(BigDecimal.valueOf(0), transactionResp.getTransactionFee());
                })
                .verifyComplete();

        verify(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository).findByCardNumber(cardDTO.getCardNumber());
        verify(transactionRepository).save(any(TransactionModel.class));

    }

    @Test
    @DisplayName("Should create a new transaction when account and card exist and is type WEB_PURCHASE ")
    void createTransaction_success_WEB_PURCHASE() throws Exception {
        transactionDTO.setTransactionType(ConstansTrType.WEB_PURCHASE);
        transactionDTO.setWebsite("website");
        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()))
                .thenReturn(Mono.just(account));
        when(cardRepository.findByCardNumber(cardDTO.getCardNumber()))
                .thenReturn(Mono.just(card));
        when(accountRepository.save(any(AccountModel.class)))
                .thenReturn(Mono.just(account));

        transaction = new BranchDeposit();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionFee(BigDecimal.valueOf(0));

        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.createTransaction(transactionDTO))
                .assertNext(transactionResp -> {
                    assertEquals(transactionDTO.getDescription(), transactionResp.getDescription());
                    assertEquals(BigDecimal.valueOf(0), transactionResp.getTransactionFee());
                })
                .verifyComplete();

        verify(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository).findByCardNumber(cardDTO.getCardNumber());
        verify(transactionRepository).save(any(TransactionModel.class));

    }
}