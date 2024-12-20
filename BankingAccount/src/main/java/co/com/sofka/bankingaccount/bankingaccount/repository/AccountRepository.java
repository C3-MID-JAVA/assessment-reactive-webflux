package co.com.sofka.bankingaccount.bankingaccount.repository;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountRepository extends ReactiveMongoRepository<BankAccount, String> {
}
