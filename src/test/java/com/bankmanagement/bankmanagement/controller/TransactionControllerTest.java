package com.bankmanagement.bankmanagement.controller;

import com.bankmanagement.bankmanagement.dto.TransactionRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.model.TransactionType;
import com.bankmanagement.bankmanagement.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    private TransactionRequestDTO validTransactionRequest;
    private TransactionResponseDTO transactionResponse;

    @BeforeEach
    void setUp(){
        validTransactionRequest = new TransactionRequestDTO(100.0, TransactionType.ATM_DEPOSIT, "123456789");
        transactionResponse = new TransactionResponseDTO("675e0ec661737976b43cca86", 2.0, 98.0, TransactionType.ATM_DEPOSIT, LocalDateTime.now());
    }

    @Test
    void create_validTransaction_ReturnsCreatedResponse() throws Exception {
        when(transactionService.create(any(TransactionRequestDTO.class))).thenReturn(transactionResponse);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTransactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("675e0ec661737976b43cca86"))
                .andExpect(jsonPath("$.fee").value(2.0))
                .andExpect(jsonPath("$.netAmount").value(98.0))
                .andExpect(jsonPath("$.type").value("ATM_DEPOSIT"));

        verify(transactionService, times(1)).create(any(TransactionRequestDTO.class));
    }

    @Test
    void create_accountNotFound_ReturnsNotFound() throws Exception {
        when(transactionService.create(any(TransactionRequestDTO.class)))
                .thenThrow(new NotFoundException("Account not found"));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTransactionRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found"));

        verify(transactionService, times(1)).create(any(TransactionRequestDTO.class));
    }

    @Test
    void create_invalidTransactionData_ReturnsBadRequest() throws Exception {
        TransactionRequestDTO invalidTransactionRequest = new TransactionRequestDTO(-100.0, TransactionType.ATM_DEPOSIT, "123456789");

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTransactionRequest)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).create(any(TransactionRequestDTO.class));
    }

    @Test
    void getAllByAccountNumber_validAccount_ReturnsTransactionList() throws Exception {
        List<TransactionResponseDTO> transactionList = List.of(transactionResponse);

        when(transactionService.getAllByAccountNumber(anyString())).thenReturn(transactionList);

        mockMvc.perform(get("/transactions/{accountNumber}/account", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("675e0ec661737976b43cca86"))
                .andExpect(jsonPath("$[0].fee").value(2.0))
                .andExpect(jsonPath("$[0].netAmount").value(98.0))
                .andExpect(jsonPath("$[0].type").value("ATM_DEPOSIT"));

        verify(transactionService, times(1)).getAllByAccountNumber("123456789");
    }

    @Test
    void getAllByAccountNumber_accountNotFound_ReturnsNotFound() throws Exception {
        when(transactionService.getAllByAccountNumber(anyString()))
                .thenThrow(new NotFoundException("Account not found"));

        mockMvc.perform(get("/transactions/{accountNumber}/account", "99999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found"));

        verify(transactionService, times(1)).getAllByAccountNumber("99999999");
    }
}