package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.dto.CardRequestDTO;
import ec.com.example.bank_account.dto.CardResponseDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.entity.Card;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.CardMapper;
import ec.com.example.bank_account.repository.CardRepository;
import ec.com.example.bank_account.service.impl.CardServiceImpl;
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
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;
    private CardRequestDTO cardRequestDTO;
    private CardResponseDTO cardResponseDTO;

    @BeforeEach
    public void setUp() {
        card = new Card();
        card.setHolderName("Diego Loor");
        card.setLimitation(new BigDecimal(1000));
        card.setCvcCode("234");
        card.setStatus("Loor");
        card.setExpirationDate(new Date());
        card.setStatus("ACTIVE");

        UserResponseDTO userResponse = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");

        TypeAccountResponseDTO typeAccountResponseDTO = new TypeAccountResponseDTO("Debit card",
                "User debit card.", "ACTIVE");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", Mono.just(userResponse), Mono.just(typeAccountResponseDTO));

        cardRequestDTO = new CardRequestDTO("Diego Loor", new BigDecimal(1000),
                "234", new Date(), "ACTIVE", "test12");
        cardResponseDTO = new CardResponseDTO("2200000000", new BigDecimal(100),
                "234", new Date(), "ACTIVE", Mono.just(accountResponseDTO));
    }

    @Test
    public void createCard_ShouldReturnCreatedEntity_WhenUserIsCreatedSuccessfully() {
        when(cardMapper.mapToEntity(cardRequestDTO)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(Mono.just(card));
        when(cardMapper.mapToDTO(card)).thenReturn(cardResponseDTO);

        Mono<CardResponseDTO> result = cardService.createCard(cardRequestDTO);

        StepVerifier.create(result)
                .expectNext(cardResponseDTO)
                .verifyComplete();

        verify(cardRepository).save(card);
        verify(cardMapper).mapToEntity(cardRequestDTO);
        verify(cardMapper).mapToDTO(card);
    }

    @Test
    public void testGetAllCards_WhenUsersExist() {
        when(cardRepository.findAll()).thenReturn(Flux.just(card));
        when(cardMapper.mapToDTO(card)).thenReturn(cardResponseDTO);

        Flux<CardResponseDTO> result = cardService.getAllCards();

        StepVerifier.create(result)
                .expectNext(cardResponseDTO)
                .verifyComplete();

        verify(cardRepository).findAll();
        verify(cardMapper).mapToDTO(card);
    }

    @Test
    public void testGetAllCards_WhenNoUsersExist() {
        when(cardRepository.findAll()).thenReturn(Flux.empty());

        Flux<CardResponseDTO> result = cardService.getAllCards();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EmptyCollectionException &&
                        throwable.getMessage().equals("No cards records found."))
                .verify();

        verify(cardRepository).findAll();
        verifyNoInteractions(cardMapper);
    }
}