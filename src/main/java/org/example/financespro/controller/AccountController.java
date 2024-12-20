package org.example.financespro.controller;

import jakarta.validation.Valid;
import org.example.financespro.dto.request.AccountRequestDto;
import org.example.financespro.dto.response.AccountResponseDto;
import org.example.financespro.facade.FinanceFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {

  private final FinanceFacade financeFacade;

  public AccountController(FinanceFacade financeFacade) {
    this.financeFacade = financeFacade;
  }

  @PostMapping
  public Mono<ResponseEntity<AccountResponseDto>> createAccount(@Valid @RequestBody AccountRequestDto request) {
    return financeFacade.createAccount(request)
            .map(account -> ResponseEntity.status(HttpStatus.CREATED).body(account))
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
  }

  @GetMapping("/{accountNumber}")
  public Mono<ResponseEntity<AccountResponseDto>> getAccount(@PathVariable String accountNumber) {
    return financeFacade.getAccountDetails(accountNumber)
            .map(account -> ResponseEntity.ok(account))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
