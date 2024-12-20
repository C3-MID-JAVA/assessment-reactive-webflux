package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.TypeTransactionRequestDTO;
import ec.com.example.bank_account.dto.TypeTransactionResponseDTO;
import ec.com.example.bank_account.entity.TypeTransaction;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.TypeTransactionMapper;
import ec.com.example.bank_account.repository.TypeTransactionRepository;
import ec.com.example.bank_account.service.impl.TypeTransactionServiceImpl;
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
public class TypeTransactionServiceImplTest {

    @Mock
    private TypeTransactionRepository typeTransactionRepository;

    @Mock
    private TypeTransactionMapper typeTransactionMapper;

    @InjectMocks
    private TypeTransactionServiceImpl typeTransactionService;

    private TypeTransaction typeTransaction;
    private TypeTransactionRequestDTO typeTransactionRequestDTO;
    private TypeTransactionResponseDTO typeTransactionResponseDTO;

    @BeforeEach
    public void setUp() {
        typeTransaction = new TypeTransaction();
        typeTransaction.setType("Deposit from branch");
        typeTransaction.setDescription("Deposit from branch.");
        typeTransaction.setTransactionCost(true);
        typeTransaction.setDiscount(false);
        typeTransaction.setValue(new BigDecimal(1));
        typeTransaction.setStatus("ACTIVE");

        typeTransactionRequestDTO = new TypeTransactionRequestDTO("Deposit from branch", "Deposit from branch.",
                new BigDecimal(1), true,false, "ACTIVE");

        typeTransactionResponseDTO = new TypeTransactionResponseDTO("Deposit from branch", "Deposit from branch.",
                new BigDecimal(1), true,false, "ACTIVE");
    }

    @Test
    public void createTypeTransaction_ShouldReturnCreatedEntity_WhenUserIsCreatedSuccessfully() {
        when(typeTransactionMapper.mapToEntity(typeTransactionRequestDTO)).thenReturn(typeTransaction);
        when(typeTransactionRepository.save(typeTransaction)).thenReturn(Mono.just(typeTransaction));
        when(typeTransactionMapper.mapToDTO(typeTransaction)).thenReturn(typeTransactionResponseDTO);

        Mono<TypeTransactionResponseDTO> result = typeTransactionService.createTypeTransaction(typeTransactionRequestDTO);

        StepVerifier.create(result)
                .expectNext(typeTransactionResponseDTO)
                .verifyComplete();

        verify(typeTransactionRepository).save(typeTransaction);
        verify(typeTransactionMapper).mapToEntity(typeTransactionRequestDTO);
        verify(typeTransactionMapper).mapToDTO(typeTransaction);
    }

    @Test
    public void testGetAllTypesTransaction_WhenUsersExist() {
        when(typeTransactionRepository.findAll()).thenReturn(Flux.just(typeTransaction));
        when(typeTransactionMapper.mapToDTO(typeTransaction)).thenReturn(typeTransactionResponseDTO);

        Flux<TypeTransactionResponseDTO> result = typeTransactionService.getAllTypeTransactions();

        StepVerifier.create(result)
                .expectNext(typeTransactionResponseDTO)
                .verifyComplete();

        verify(typeTransactionRepository).findAll();
        verify(typeTransactionMapper).mapToDTO(typeTransaction);
    }

    @Test
    public void testGetAllTypesTransaction_WhenNoUsersExist() {
        when(typeTransactionRepository.findAll()).thenReturn(Flux.empty());

        Flux<TypeTransactionResponseDTO> result = typeTransactionService.getAllTypeTransactions();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EmptyCollectionException &&
                        throwable.getMessage().equals("No typesTransaction records found."))
                .verify();

        verify(typeTransactionRepository).findAll();
        verifyNoInteractions(typeTransactionMapper);
    }
}