package com.sofkau.usrv_accounts_manager.services;

import com.sofkau.usrv_accounts_manager.Utils.Utils;
import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.dto.CardDTO;
import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.model.CardModel;
import com.sofkau.usrv_accounts_manager.repository.AccountRepository;
import com.sofkau.usrv_accounts_manager.repository.CardRepository;
import com.sofkau.usrv_accounts_manager.services.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.smartcardio.Card;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CardServiceTest {


    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private CardModel card;
    private CardDTO cardDTO;
    private AccountDTO accountDTO;
    private AccountModel account;

    @BeforeEach
    void setUp() {
        card = new CardModel();
        card.setCardNumber("123456789");
        card.setCardCVV("1234");
        accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456");
        cardDTO = new CardDTO("CARD TEST", "123456789",
                "TDEBIT", "ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO, null
        );
        account = new AccountModel();
        account.setAccountNumber("1234599");
    }

    @Test
    @DisplayName("Should find an existing cvv and return true")
    void existsCvv_Exists_ReturnTrue() {
        String existingCvv = "123";
        when(cardRepository.existsByCardCVV(existingCvv)).thenReturn(Mono.just(true));

        StepVerifier.create(cardService.existsCvv(existingCvv))
                .expectNext(true)
                .verifyComplete();

        verify(cardRepository).existsByCardCVV(existingCvv);
    }

    @Test
    @DisplayName("Should not find a existing cvv and return false")
    void existsCvv_Exists_ReturnFalse() {
        String notExistingCvv = "123";

        when(cardRepository.existsByCardCVV(notExistingCvv)).thenReturn(Mono.just(false));

        StepVerifier.create(cardService.existsCvv(notExistingCvv))
                .expectNext(false)
                .verifyComplete();
        verify(cardRepository).existsByCardCVV(notExistingCvv);

    }


    @Test
    @DisplayName("Should create a new card when card does NOT already exist")
    void createCard_success() throws Exception {

        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber()))
                .thenReturn(Mono.just(account));
        when(cardRepository.findByCardNumber(cardDTO.getCardNumber()))
                .thenReturn(Mono.empty());
        when(cardRepository.save(any(CardModel.class))).thenReturn(Mono.just(card));
        when(cardRepository.existsByCardCVV(anyString())).thenReturn(Mono.just(false));




        StepVerifier.create(cardService.createCard(cardDTO))
                .assertNext(carDTORes -> assertEquals(cardDTO.getCardNumber(), carDTORes.getCardNumber()))
                .verifyComplete();


        verify(accountRepository).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository).findByCardNumber(cardDTO.getCardNumber());
        verify(cardRepository).save(any(CardModel.class));
        verify(cardRepository).existsByCardCVV(anyString());


    }

    @Test
    @DisplayName("Should NOT create a new card when card already exist")
    void createCard_cardAlreadyExists() {

        when(cardRepository.findByCardNumber(cardDTO.getCardNumber()))
                .thenReturn(Mono.just(card));

        StepVerifier.create(cardService.createCard(cardDTO))
                .expectErrorMatches(err -> err.getMessage().equals("Card already exists"))
                .verify();

        verify(accountRepository, never()).findByAccountNumber(accountDTO.getAccountNumber());
        verify(cardRepository).findByCardNumber(cardDTO.getCardNumber());
        verify(cardRepository, never()).save(any(CardModel.class));
        verify(cardRepository, never()).existsByCardCVV(anyString());
    }
}