package com.sofkau.usrv_accounts_manager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.dto.AccountSimpleRequestDTO;
import com.sofkau.usrv_accounts_manager.dto.CardDTO;
import com.sofkau.usrv_accounts_manager.services.AccountService;
import com.sofkau.usrv_accounts_manager.services.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CardControllerTest {

    @Autowired
    private WebTestClient webTestclient;

    @MockitoBean
    private CardService cardService;


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should create a new card and return the complete body response")
    void createAccount() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456");
        CardDTO cardDTORequest = new CardDTO("CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO, null
                );
        CardDTO cardDTOResponse = new CardDTO("CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO, null
        );


        when(cardService.createCard(any(CardDTO.class))).thenReturn(Mono.just(cardDTOResponse));


        webTestclient
                .post()
                .uri("/api/v1/card/create")
                .bodyValue(cardDTORequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CardDTO.class)
                .consumeWith(response -> {
                    CardDTO actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(cardDTORequest.getCardNumber(), actualResponse.getCardNumber());
                    assertEquals(cardDTORequest.getAccount().getAccountNumber(), actualResponse.getAccount().getAccountNumber());
                });

        verify(cardService, times(1)).createCard(any(CardDTO.class));

    }

    @Test
    @DisplayName("Should retrieve all cards by accountNumber when exists")
    void findByAccount() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456");
        CardDTO cardDTO = new CardDTO("CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO, null
        );
        CardDTO cardDTO2 = new CardDTO("CARD TEST", "123456789",
                "TDEBIT","ACTIVE", "12-12-2024",
                BigDecimal.valueOf(1000), "TEST HOLDER",
                accountDTO, null
        );
        AccountSimpleRequestDTO accountDTO2 = new AccountSimpleRequestDTO();
        accountDTO2.setAccountNumber("123456");


        when(cardService.getCardsByAccount(anyString())).thenReturn(Flux.just(cardDTO, cardDTO2));


        webTestclient
                .post()
                .uri("/api/v1/card/byAccount")
                .bodyValue(accountDTO2)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CardDTO.class)
                .consumeWith(response -> {
                    List<CardDTO> actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(cardDTO.getCardNumber(), actualResponse.get(0).getCardNumber());
                    assertEquals(cardDTO2.getCardNumber(), actualResponse.get(1).getCardNumber());
                });

        verify(cardService, times(1)).getCardsByAccount(anyString());

    }

}