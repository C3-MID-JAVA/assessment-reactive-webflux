package org.example.financespro.controller;

import jakarta.validation.Valid;
import org.example.financespro.dto.request.TransactionRequestDto;
import org.example.financespro.dto.response.TransactionResponseDto;
import org.example.financespro.facade.FinanceFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
@Validated // Activa la validación global para el controlador
public class TransactionController {

  private final FinanceFacade financeFacade;

  public TransactionController(FinanceFacade financeFacade) {
    this.financeFacade = financeFacade;
  }

  @PostMapping
  public Mono<ResponseEntity<TransactionResponseDto>> processTransaction(
      @Valid @RequestBody TransactionRequestDto request) { // Valida solo este parámetro
    return financeFacade
        .processTransaction(request)
        .map(transaction -> ResponseEntity.ok(transaction))
        .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
  }
}
