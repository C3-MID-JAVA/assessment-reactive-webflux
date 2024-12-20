package es.cuenta_bancaria_webflux.controllers;

import es.cuenta_bancaria_webflux.dto.AccountDTO;
import es.cuenta_bancaria_webflux.dto.TransactionDTO;
import es.cuenta_bancaria_webflux.service.ITransactionService;
import es.cuenta_bancaria_webflux.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/transacciones")
public class TransactionController {
    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<TransactionDTO>>> listarTransacciones() {
        return transactionService.listarTransacciones()
                .collectList()  // Convierte el Flux a una lista
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.ok().body(Flux.fromIterable(list)));
                    }
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionDTO>> obtenerTransaccionPorId(@PathVariable String id) {
        if (id == null || id.trim().isEmpty() || id.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().build()); // 400 BAD_REQUEST si el ID no es válido
        }
        return transactionService.obtenerTransaccionPorId(id)
                .map(ResponseEntity::ok)  // Si la cuenta existe, devuelve 200 OK con la cuenta
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));  // Si no se encuentra la cuenta, devuelve 204 NO_CONTENT
    }

    @GetMapping("/cuenta/{idCuenta}")
    public Mono<ResponseEntity<Flux<TransactionDTO>>> listarTransaccionesPorCuenta(@PathVariable String idCuenta) {
        return transactionService.obtenerTransaccionesPorCuenta(idCuenta)
                .collectList()  // Convierte el Flux a una lista
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.ok().body(Flux.fromIterable(list)));
                    }
                });
    }

}
