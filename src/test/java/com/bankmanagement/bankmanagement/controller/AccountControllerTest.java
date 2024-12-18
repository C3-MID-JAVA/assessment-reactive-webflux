package com.bankmanagement.bankmanagement.controller;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    private AccountRequestDTO validAccountRequest;
    private AccountResponseDTO accountResponse;

    @BeforeEach
    void setUp() {
        validAccountRequest = new AccountRequestDTO("675e0e1259d6de4eda5b29b7");
        accountResponse = new AccountResponseDTO("12345678", 0.0, "675e0e1259d6de4eda5b29b7");
    }

    @Test
    void create_validAccount_ReturnsCreatedResponse() throws Exception {
        when(accountService.create(any(AccountRequestDTO.class))).thenReturn(accountResponse);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAccountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("12345678"))
                .andExpect(jsonPath("$.balance").value(0.0))
                .andExpect(jsonPath("$.userId").value("675e0e1259d6de4eda5b29b7"));

        verify(accountService, times(1)).create(any(AccountRequestDTO.class));
    }

    @Test
    void register_DuplicateUser_ReturnsBadRequest() throws Exception {
        when(accountService.create(any(AccountRequestDTO.class)))
                .thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAccountRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));

        verify(accountService, times(1)).create(any(AccountRequestDTO.class));
    }

    @Test
    void register_EmptyDocumentId_ReturnsBadRequest() throws Exception {
        AccountRequestDTO invalidAccountRequest = new AccountRequestDTO();
        invalidAccountRequest.setUserId("");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccountRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).create(any(AccountRequestDTO.class));
    }

    @Test
    void getAllByUserId_validUser_ReturnsAccountsList() throws Exception {
        List<AccountResponseDTO> accountList = List.of(accountResponse);

        when(accountService.getAllByUserId(anyString())).thenReturn(accountList);

        mockMvc.perform(get("/accounts/{userId}/user", "675e0e1259d6de4eda5b29b7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("12345678"))
                .andExpect(jsonPath("$[0].balance").value(0.0))
                .andExpect(jsonPath("$[0].userId").value("675e0e1259d6de4eda5b29b7"));

        verify(accountService, times(1)).getAllByUserId("675e0e1259d6de4eda5b29b7");
    }

    @Test
    void getByAccountNumber_validAccount_ReturnsAccount() throws Exception {
        when(accountService.findByAccountNumber(anyString())).thenReturn(accountResponse);

        mockMvc.perform(get("/accounts/{id}", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345678"))
                .andExpect(jsonPath("$.balance").value(0.0))
                .andExpect(jsonPath("$.userId").value("675e0e1259d6de4eda5b29b7"));

        verify(accountService, times(1)).findByAccountNumber("12345678");
    }

    @Test
    void getByAccountNumber_accountNotFound_ReturnsNotFound() throws Exception {
        when(accountService.findByAccountNumber(anyString()))
                .thenThrow(new NotFoundException("Account not found"));

        mockMvc.perform(get("/accounts/{id}", "99999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found"));

        verify(accountService, times(1)).findByAccountNumber("99999999");
    }
}
