package org.example.financespro.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.financespro.dto.request.TransactionRequestDto;
import org.example.financespro.dto.response.TransactionResponseDto;
import org.example.financespro.facade.FinanceFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FinanceFacade financeFacade;

  @Test
  void shouldProcessTransactionSuccessfully() throws Exception {
    TransactionRequestDto request = new TransactionRequestDto("123", "ATM_WITHDRAWAL", BigDecimal.valueOf(500));
    TransactionResponseDto response = new TransactionResponseDto(
            "ATM_WITHDRAWAL",
            BigDecimal.valueOf(500),
            BigDecimal.valueOf(1.00),
            BigDecimal.valueOf(499)
    );

    Mockito.when(financeFacade.processTransaction(any(TransactionRequestDto.class)))
            .thenReturn(Mono.just(response));

    mockMvc.perform(post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionType").value("ATM_WITHDRAWAL"))
            .andExpect(jsonPath("$.transactionAmount").value(500))
            .andExpect(jsonPath("$.transactionCost").value(1.00))
            .andExpect(jsonPath("$.remainingBalance").value(499));
  }

  @Test
  void shouldReturnBadRequestForInvalidTransaction() throws Exception {
    TransactionRequestDto request = new TransactionRequestDto("", "", BigDecimal.valueOf(-1));

    mockMvc.perform(post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
}
