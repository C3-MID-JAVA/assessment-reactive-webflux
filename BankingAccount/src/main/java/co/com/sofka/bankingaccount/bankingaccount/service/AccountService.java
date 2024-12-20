package co.com.sofka.bankingaccount.bankingaccount.service;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.TransactionDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.CreateAccountRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseDTO;
import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.repository.AccountRepository;
import co.com.sofka.bankingaccount.bankingaccount.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<BankAccount> createAccount(CreateAccountRequestDTO createAccountRequestDTO){
        if (createAccountRequestDTO.getEntitledUserName() == null){
            throw new NullPointerException();
        }

        BankAccount createdAccount = new BankAccount();
        createdAccount.setBalance(createAccountRequestDTO.getBalance());
        createdAccount.setEntitledUser(createAccountRequestDTO.getEntitledUserName());

        return accountRepository.save(createdAccount);
    }

    public Mono<BankAccount> getAccount(String id){
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found!")));
    }

    public Mono<BigDecimal> executeTransaction(String accountId, TransactionBase transaction) {
        return getAccount(accountId)
                .flatMap(account -> {
                    BigDecimal impact = transaction.calculateImpact();
                    account.applyTransaction(impact);
                    transaction.setAccountId(accountId);
                    return transactionRepository.save(transaction)
                            .then(accountRepository.save(account))
                            .map(BankAccount::getBalance);
                });
    }

    public Flux<TransactionBase> getTransactionsByAccount(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Mono<AccountResponseDTO> getAccountWithTransaction(String id) {
        return getAccount(id)
                .flatMap(account -> getTransactionsByAccount(account.getId())
                        .map(transaction -> new TransactionDTO(
                                transaction.getId(),
                                transaction.getType(),
                                transaction.getAmount(),
                                transaction.getCost()))
                        .collectList()
                        .map(transactions -> new AccountResponseDTO(
                                account.getId(),
                                account.getBalance(),
                                transactions)));
    }
}
