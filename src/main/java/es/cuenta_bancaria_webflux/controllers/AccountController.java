package es.cuenta_bancaria_webflux.controllers;
import es.cuenta_bancaria_webflux.dto.AccountDTO;
import es.cuenta_bancaria_webflux.exception.CuentaNoEncontradaException;
import es.cuenta_bancaria_webflux.exception.SaldoInsuficienteException;
import es.cuenta_bancaria_webflux.model.Transaction;
import es.cuenta_bancaria_webflux.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cuentas")
public class AccountController {

    private final IAccountService accountService;
    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<AccountDTO>>> listarCuentas() {
        return accountService.listarCuentas()
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
    public Mono<ResponseEntity<AccountDTO>> obtenerCuentaPorId(@PathVariable String id) {
        return accountService.obtenerCuentaPorId(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(CuentaNoEncontradaException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
                )  // Manejar la excepción específica y devolver un 404
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    // Crear una cuenta
    @PostMapping
    public Mono<ResponseEntity<AccountDTO>> crearCuenta(@Validated @RequestBody AccountDTO accountDTO) {
        System.out.println("Titular: " + accountDTO.getTitular());
        System.out.println("Saldo: " + accountDTO.getSaldo());
        return accountService.crearCuenta(accountDTO)
                .map(cuenta -> ResponseEntity.status(HttpStatus.CREATED).body(cuenta))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null))) // Cuando el servicio devuelve Mono.empty()
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null))); // En caso de una excepción
    }



    // Realizar transacción
    @PostMapping("/{id}/transacciones")
    public Mono<ResponseEntity<AccountDTO>> realizarTransaccion(
            @PathVariable String id,
            @RequestBody Transaction transaction) {  // Cambié @RequestParam por @RequestBody
        return accountService.realizarTransaccion(id, transaction.getMonto(), transaction.getTipo())
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(CuentaNoEncontradaException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(null)) // Devuelve un 404 con el mensaje
                )
                .onErrorResume(SaldoInsuficienteException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(null)) // Devuelve un 400 con el mensaje de saldo insuficiente
                )
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(null)) // Devuelve un 400 con el mensaje de tipo no válido
                )
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null)) // Manejo genérico de otros errores
                );
    }
}
