package com.reactivo.banco.controller;

import com.reactivo.banco.model.dto.ClientInDTO;
import com.reactivo.banco.model.dto.ClientOutDTO;
import com.reactivo.banco.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/clientes")
@Tag(name = "Clients", description = "Endpoints for client management")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Create a new client",
            description = "Registers a new client in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public Mono<ClientOutDTO> saveClient(@Valid @RequestBody ClientInDTO clientInDTO) {
        return clientService.saveClient(clientInDTO);
    }

    @Operation(summary = "Retrieve the list of all clients",
            description = "Returns a list with information about all registered clients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully")
    })
    @GetMapping
    public Flux<ClientOutDTO> getAll() {
        return clientService.getAll();
    }

    @Operation(summary = "Retrieve client by ID",
            description = "Fetches information about a specific client by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public Mono<ClientOutDTO> getById(@PathVariable String id) {
        return clientService.getById(id);
    }

    @Operation(summary = "Update an existing client",
            description = "Updates the information of a specific client by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping("/{id}")
    public Mono<ClientOutDTO> updateClient(@PathVariable String id, @Valid @RequestBody ClientInDTO clientInDTO) {
        return clientService.updateClient(id, clientInDTO);
    }

    @Operation(summary = "Delete a client",
            description = "Deletes a specific client by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{id}")
    public Mono<Void> deleteClient(@PathVariable String id) {
        return clientService.deleteClient(id);
    }
}
