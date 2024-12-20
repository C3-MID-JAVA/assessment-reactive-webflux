package edisonrmedina.CityBank.controller;

import edisonrmedina.CityBank.dto.TransactionDTO;
import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.entity.transaction.Transaction;
import edisonrmedina.CityBank.mapper.Mapper;
import edisonrmedina.CityBank.service.BankAccountService;
import edisonrmedina.CityBank.service.TransactionsService;
import edisonrmedina.CityBank.service.impl.BankAccountServiceImp;
import edisonrmedina.CityBank.service.impl.TransactionsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionsServiceImpl transactionsService;
    private final BankAccountServiceImp bankAccountService;

    public TransactionsController(TransactionsServiceImpl transactionsServiceImp, BankAccountServiceImp bankAccountServiceImpl) {
        this.transactionsService = transactionsServiceImp;
        this.bankAccountService = bankAccountServiceImpl;
    }

    @Operation(
            summary = "Obtener todas las transacciones",
            description = "Devuelve una lista con todas las transacciones registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Flux<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionsService.getAllTransactions());
    }

    @Operation(
            summary = "Crear una nueva transacción",
            description = "Registra una nueva transacción asociada a una cuenta bancaria existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transacción creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta bancaria no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    @PostMapping("/transactions/{accountId}")
    public Mono<ResponseEntity<Transaction>> createTransaction(
            @PathVariable String accountId,
            @RequestBody Mono<TransactionDTO> transactionRequestMono) {

        return bankAccountService.getBankAccount(accountId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta bancaria no encontrada")))
                .flatMap(bankAccount -> transactionRequestMono
                        .flatMap(transactionRequest -> {
                            // Mapear DTO a entidad Transaction
                            Transaction transaction = Mapper.dtoToTransaction(transactionRequest, bankAccount);

                            // Llamada única a createTransaction
                            return transactionsService.createTransaction(transaction); // Guardar transacción
                        })
                )
                .map(savedTransaction -> ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction)) // Respuesta 201
                .onErrorResume(e -> {
                    if (e instanceof ResponseStatusException) {
                        return Mono.just(ResponseEntity.status(((ResponseStatusException) e).getStatusCode()).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

}
