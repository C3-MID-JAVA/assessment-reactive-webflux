package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.TypeAccountRequestDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.service.TypeAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@Tag(name = "Account type RESTful", description = "Endpoints for Account type management.")
@RestController
@RequestMapping("/api/types-account")
public class TypeAccountController {

    private final TypeAccountService typeAccountService;

    public TypeAccountController(TypeAccountService typeAccountService) {
        this.typeAccountService = typeAccountService;
    }

    @Operation(summary = "Create new account type", description = "Create a new account type from the request data.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account type created successfully."),
            @ApiResponse(responseCode = "404", description = "No resources found."),
            @ApiResponse(responseCode = "500", description = "Internal application problems.")
    })
    @PostMapping
    public Mono<ResponseEntity<TypeAccountResponseDTO>> createTypeAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Body with the account type data to be created", required = true)
            @Valid @RequestBody TypeAccountRequestDTO typeAccount) {
        return typeAccountService.createTypeAccount(typeAccount)
                .map(typeAccountResponse -> ResponseEntity.status(HttpStatus.CREATED).body(typeAccountResponse));
    }

    @Operation(summary = "Get all account type", description = "Get all registered account types.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully obtained all registered account types."),
            @ApiResponse(responseCode = "404", description = "No resources found."),
            @ApiResponse(responseCode = "500", description = "Internal application problems.")
    })
    @GetMapping
    public Mono<ResponseEntity<List<TypeAccountResponseDTO>>> getTypesAccount() {
        return typeAccountService.getAllTypeAccount()
                .collectList()
                .map(ResponseEntity::ok);
    }
}