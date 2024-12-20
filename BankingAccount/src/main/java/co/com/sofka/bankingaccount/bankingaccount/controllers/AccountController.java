package co.com.sofka.bankingaccount.bankingaccount.controllers;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.CreateAccountRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.TransactionRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseTransactionDTO;
import co.com.sofka.bankingaccount.bankingaccount.factory.TransactionFactory;
import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bankAccount")
public class AccountController {
    private final AccountService accountService;
    private final TransactionFactory transactionFactory;

    public AccountController(AccountService accountService, TransactionFactory transactionFactory) {
        this.accountService = accountService;
        this.transactionFactory = transactionFactory;
    }

    @PostMapping
    public Mono<ResponseEntity<BankAccount>> createBankAccount(@RequestBody CreateAccountRequestDTO createAccountRequestDTO) {
        return accountService.createAccount(createAccountRequestDTO)
                .map(account -> new ResponseEntity<>(account, HttpStatus.CREATED))
                .onErrorResume(throwable -> Mono.error(throwable));
    }

    @PostMapping("/transaction")
    public Mono<ResponseEntity<AccountResponseTransactionDTO>> executeTransaction(
            @RequestBody @Valid TransactionRequestDTO transactionDTO) {
        return accountService.getAccount(transactionDTO.getId())
                .flatMap(account -> {
                    TransactionBase transaction = transactionFactory.createTransaction(
                            transactionDTO.getType(),
                            transactionDTO.getAmount(),
                            transactionDTO.getId()
                    );
                    return accountService.executeTransaction(account.getId(), transaction)
                            .map(balance -> new ResponseEntity<>(
                                    new AccountResponseTransactionDTO(account.getId(), balance),
                                    HttpStatus.CREATED
                            ));
                });
    }

    @GetMapping
    public Mono<ResponseEntity<AccountResponseDTO>> getAccountWithTransactions(@RequestParam("id") String id) {
        return accountService.getAccountWithTransaction(id)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)));
    }
}
