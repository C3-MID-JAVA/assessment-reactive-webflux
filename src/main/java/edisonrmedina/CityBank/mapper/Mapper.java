package edisonrmedina.CityBank.mapper;

import edisonrmedina.CityBank.dto.BankAccountDTO;
import edisonrmedina.CityBank.dto.CreateBankAccountDTO;
import edisonrmedina.CityBank.dto.TransactionDTO;
import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.entity.transaction.Transaction;
import edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy.TransactionCostStrategy;
import edisonrmedina.CityBank.enums.TransactionType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Mapper {

    private final Map<TransactionType, TransactionCostStrategy> strategies = new HashMap<>();

    public static BankAccountDTO bankAccountToDTO(BankAccount bankAccount) {
        return BankAccountDTO
                .builder()
                .id(bankAccount.getId())
                .accountHolder(bankAccount.getAccountHolder())
                .balance(bankAccount.getBalance())
                .build();
    }

    public static BankAccount dtoToBankAccount(BankAccountDTO bankAccountDTO) {
        return new BankAccount(bankAccountDTO.getAccountHolder(),bankAccountDTO.getBalance());
    }

    // Conversión reactiva de CreateBankAccountDTO a BankAccount (dentro de Mono)
    public static Mono<CreateBankAccountDTO> dtoMonoToDtoToBankAccount(Mono<CreateBankAccountDTO> createBankAccountDTOMono) {
        return createBankAccountDTOMono.map(dto -> new CreateBankAccountDTO(dto.getAccountHolder(), dto.getBalance()));
    }

    public static TransactionDTO transactionToDto(Transaction transaction) {
        return new TransactionDTO(
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBankAccount().getId(),
                transaction.getTransactionCost()
        );
    }

    public static Transaction dtoToTransaction(TransactionDTO transactionDTO, BankAccount bankAccountMono) {
        // Desenrollamos el Mono<BankAccount> directamente dentro del método.

       return new Transaction(
                transactionDTO.getType(),
                transactionDTO.getAmount(),
                transactionDTO.getTransactionCost(),
                bankAccountMono // Aquí utilizamos directamente el objeto BankAccount desenvuelto.
        );

    }
}
