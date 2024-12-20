package edisonrmedina.CityBank.controller;

import edisonrmedina.CityBank.dto.BankAccountDTO;
import edisonrmedina.CityBank.dto.CreateBankAccountDTO;
import edisonrmedina.CityBank.mapper.Mapper;
import edisonrmedina.CityBank.service.BankAccountService;
import edisonrmedina.CityBank.service.impl.BankAccountServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {

    private final BankAccountServiceImp bankAccountServiceImp;

    @Autowired
    public BankAccountController(BankAccountServiceImp bankAccountServiceImp) {
        this.bankAccountServiceImp = bankAccountServiceImp;
    }

    @Operation(
            summary = "Obtener detalles de una cuenta bancaria",
            description = "Devuelve los detalles de una cuenta bancaria existente especificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta bancaria encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta bancaria no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Cuenta bancaria no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BankAccountDTO>> getBankAccount(@PathVariable String id) {
        return bankAccountServiceImp.getBankAccount(id)
                .map(bankAccount -> ResponseEntity.ok(Mapper.bankAccountToDTO(bankAccount))) // Respuesta con 200 OK
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))); // Respuesta con 404
    }

    @Operation(
            summary = "Crear una nueva cuenta bancaria",
            description = "Registra una nueva cuenta bancaria en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta bancaria creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<BankAccountDTO>> createBankAccount(Mono<CreateBankAccountDTO> createBankAccountDTOMono) {
        return bankAccountServiceImp.register(createBankAccountDTOMono)
                .map(createdAccount -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(createdAccount)) // Devuelve 201 con el cuerpo
                .onErrorResume(e -> Mono.just(
                        ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(null))); // Devuelve 400 en caso de error
    }

}
