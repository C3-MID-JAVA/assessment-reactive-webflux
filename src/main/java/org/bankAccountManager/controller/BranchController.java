package org.bankAccountManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bankAccountManager.DTO.request.BranchRequestDTO;
import org.bankAccountManager.DTO.response.BranchResponseDTO;
import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.service.implementations.BranchServiceImplementation;
import org.bankAccountManager.service.interfaces.BranchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.bankAccountManager.mapper.DTORequestMapper.toBranch;
import static org.bankAccountManager.mapper.DTOResponseMapper.toBranchResponseDTO;

@Tag(name = "Branch Management", description = "Endpoints for managing branches")
@RestController
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchServiceImplementation branchService) {
        this.branchService = branchService;
    }

    @Operation(summary = "Create a new branch", description = "Add a new branch to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Branch created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Mono<ResponseEntity<BranchResponseDTO>> createBranch(@RequestBody BranchRequestDTO branch) {
        return branchService.createBranch(toBranch(Mono.just(branch)))
                .flatMap(branchEntity -> toBranchResponseDTO(Mono.just(branchEntity))
                        .map(branchResponseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(branchResponseDTO)));
    }

    @Operation(summary = "Retrieve a branch by ID", description = "Get the details of a branch using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Branch retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @PostMapping("/id")
    public Mono<ResponseEntity<BranchResponseDTO>> getBranchById(@RequestBody BranchRequestDTO branch) {
        return branchService.getBranchById(toBranch(Mono.just(branch))
                        .map(Branch::getId))
                .flatMap(branchEntity -> toBranchResponseDTO(Mono.just(branchEntity))
                        .map(branchResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(branchResponseDTO)));
    }

    @Operation(summary = "Retrieve a branch by name", description = "Get the details of a branch using its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Branch retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @PostMapping("/name")
    public Mono<ResponseEntity<BranchResponseDTO>> getBranchByName(@RequestBody BranchRequestDTO branch) {
        return branchService.getBranchByName(toBranch(Mono.just(branch))
                        .map(Branch::getName))
                .flatMap(branchEntity -> toBranchResponseDTO(Mono.just(branchEntity))
                        .map(branchResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(branchResponseDTO)));
    }

    @Operation(summary = "Retrieve all branches", description = "Get the list of all branches")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branches retrieved successfully")
    })
    @GetMapping
    public Mono<ResponseEntity<Flux<BranchResponseDTO>>> getAllBranches() {
        return Mono.just(
                ResponseEntity.ok(
                        branchService.getAllBranches()
                                .flatMap(branchEntity -> toBranchResponseDTO(Mono.just(branchEntity)))
                )
        );
    }

    @Operation(summary = "Update a branch", description = "Update an existing branch with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @PutMapping
    public Mono<ResponseEntity<BranchResponseDTO>> updateBranch(@RequestBody BranchRequestDTO branch) {
        return branchService.updateBranch(toBranch(Mono.just(branch)))
                .flatMap(branchEntity -> toBranchResponseDTO(Mono.just(branchEntity))
                        .map(branchResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(branchResponseDTO)));
    }

    @Operation(summary = "Delete a branch", description = "Delete a branch by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Branch deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteBranch(@RequestBody BranchRequestDTO branch) {
        return toBranch(Mono.just(branch)) // Convierte el DTO a la entidad Account
                .flatMap(branchEntity ->
                        branchService.deleteBranch(Mono.just(branchEntity.getId()))
                                .thenReturn(ResponseEntity.noContent().<Void>build())
                                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()))
                );
    }
}
