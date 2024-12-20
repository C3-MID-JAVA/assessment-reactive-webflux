package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.TypeAccountRequestDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.entity.TypeAccount;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.TypeAccountMapper;
import ec.com.example.bank_account.repository.TypeAccountRepository;
import ec.com.example.bank_account.service.impl.TypeAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TypeAccountServiceImplTest {
    
    @Mock
    private TypeAccountRepository typeAccountRepository;

    @Mock
    private TypeAccountMapper typeAccountMapper;

    @InjectMocks
    private TypeAccountServiceImpl typeAccountService;

    private TypeAccount typeAccount;
    private TypeAccountRequestDTO typeAccountRequestDTO;
    private TypeAccountResponseDTO typeAccountResponseDTO;

    @BeforeEach
    public void setUp() {
        typeAccount = new TypeAccount();
        typeAccount.setType("Debit account");
        typeAccount.setDescription("User debit account.");
        typeAccount.setStatus("ACTIVE");

        typeAccountRequestDTO = new TypeAccountRequestDTO("Debit account", "User debit account.", "ACTIVE");

        typeAccountResponseDTO = new TypeAccountResponseDTO("Debit account", "User debit account.", "ACTIVE");
    }

    @Test
    public void createTypeTransaction_ShouldReturnCreatedEntity_WhenUserIsCreatedSuccessfully() {
        when(typeAccountMapper.mapToEntity(typeAccountRequestDTO)).thenReturn(typeAccount);
        when(typeAccountRepository.save(typeAccount)).thenReturn(Mono.just(typeAccount));
        when(typeAccountMapper.mapToDTO(typeAccount)).thenReturn(typeAccountResponseDTO);

        Mono<TypeAccountResponseDTO> result = typeAccountService.createTypeAccount(typeAccountRequestDTO);

        StepVerifier.create(result)
                .expectNext(typeAccountResponseDTO)
                .verifyComplete();

        verify(typeAccountRepository).save(typeAccount);
        verify(typeAccountMapper).mapToEntity(typeAccountRequestDTO);
        verify(typeAccountMapper).mapToDTO(typeAccount);
    }

    @Test
    public void testGetAllTypesAccount_WhenUsersExist() {
        when(typeAccountRepository.findAll()).thenReturn(Flux.just(typeAccount));
        when(typeAccountMapper.mapToDTO(typeAccount)).thenReturn(typeAccountResponseDTO);

        Flux<TypeAccountResponseDTO> result = typeAccountService.getAllTypeAccount();

        StepVerifier.create(result)
                .expectNext(typeAccountResponseDTO)
                .verifyComplete();

        verify(typeAccountRepository).findAll();
        verify(typeAccountMapper).mapToDTO(typeAccount);
    }

    @Test
    public void testGetAllTypesAccount_WhenNoUsersExist() {
        when(typeAccountRepository.findAll()).thenReturn(Flux.empty());

        Flux<TypeAccountResponseDTO> result = typeAccountService.getAllTypeAccount();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EmptyCollectionException &&
                        throwable.getMessage().equals("No typesAccount records found."))
                .verify();

        verify(typeAccountRepository).findAll();
        verifyNoInteractions(typeAccountMapper);
    }
}