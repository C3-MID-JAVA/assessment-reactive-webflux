package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.AccountRequestDTO;
import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.entity.Account;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.AccountMapper;
import ec.com.example.bank_account.repository.AccountRepository;
import ec.com.example.bank_account.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountRequestDTO accountRequestDTO;
    private AccountResponseDTO accountResponseDTO;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setNumber("2200000000");
        account.setAvailableBalance(new BigDecimal(100));
        account.setRetainedBalance(new BigDecimal(0));
        account.setStatus("ACTIVE");

        UserResponseDTO userResponseDTO = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");

        TypeAccountResponseDTO typeAccountResponseDTO = new TypeAccountResponseDTO("Debit account",
                "User debit account.", "ACTIVE");

        accountRequestDTO = new AccountRequestDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", "test", "test");
        accountResponseDTO = new AccountResponseDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", Mono.just(userResponseDTO), Mono.just(typeAccountResponseDTO));
    }

    @Test
    public void createAccount_ShouldReturnCreatedEntity_WhenUserIsCreatedSuccessfully() {
        when(accountMapper.mapToEntity(accountRequestDTO)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(Mono.just(account));
        when(accountMapper.mapToDTO(account)).thenReturn(accountResponseDTO);

        Mono<AccountResponseDTO> result = accountService.createAccount(accountRequestDTO);

        StepVerifier.create(result)
                .expectNext(accountResponseDTO)
                .verifyComplete();

        verify(accountRepository).save(account);
        verify(accountMapper).mapToEntity(accountRequestDTO);
        verify(accountMapper).mapToDTO(account);
    }

    @Test
    public void testGetAllAccounts_WhenUsersExist() {
        when(accountRepository.findAll()).thenReturn(Flux.just(account));
        when(accountMapper.mapToDTO(account)).thenReturn(accountResponseDTO);

        Flux<AccountResponseDTO> result = accountService.getAllAccounts();

        StepVerifier.create(result)
                .expectNext(accountResponseDTO)
                .verifyComplete();

        verify(accountRepository).findAll();
        verify(accountMapper).mapToDTO(account);
    }

    @Test
    public void testGetAllAccounts_WhenNoUsersExist() {
        when(accountRepository.findAll()).thenReturn(Flux.empty());

        Flux<AccountResponseDTO> result = accountService.getAllAccounts();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EmptyCollectionException &&
                        throwable.getMessage().equals("No accounts records found."))
                .verify();

        verify(accountRepository).findAll();
        verifyNoInteractions(accountMapper);
    }
}