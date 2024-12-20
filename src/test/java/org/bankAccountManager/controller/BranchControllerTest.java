package org.bankAccountManager.controller;

import org.bankAccountManager.DTO.request.BranchRequestDTO;
import org.bankAccountManager.DTO.response.BranchResponseDTO;
import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.service.implementations.BranchServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BranchController.class)
class BranchControllerTest {

    /*@Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BranchServiceImplementation branchService;

    private BranchRequestDTO branchRequest;
    private BranchResponseDTO branchResponse;
    private Branch branch;

    @BeforeEach
    void setUp() {
        branchRequest = new BranchRequestDTO();
        branchRequest.setId(1);
        branchRequest.setName("Main Branch");
        branchRequest.setAddress("Main Branch Address");
        branchRequest.setPhone("Main Branch Phone");

        branchResponse = new BranchResponseDTO();
        branchResponse.setId(1);
        branchResponse.setName("Main Branch");
        branchResponse.setAddress("Main Branch Address");
        branchResponse.setPhone("Main Branch Phone");

        branch = new Branch();
        branch.setId(1);
        branch.setName("Main Branch");
        branch.setAddress("Main Branch Address");
        branch.setPhone("Main Branch Phone");
    }

    @Test
    void createBranch_success() {
        when(branchService.createBranch(Mono.just(any(Branch.class))))
                .thenReturn(Mono.just(branch));
        webTestClient.post()
                .uri("/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(branchRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BranchResponseDTO.class)
                .isEqualTo(branchResponse);
    }

    @Test
    void createBranch_invalidInput() {
        webTestClient.post()
                .uri("/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BranchRequestDTO())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getBranchById_success() {
        when(branchService.getBranchById(Mono.just(1)))
                .thenReturn(Mono.just(branch));
        webTestClient.post()
                .uri("/branches/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(branchRequest)
                .exchange()
                .expectStatus().isFound()
                .expectBody(BranchResponseDTO.class)
                .isEqualTo(branchResponse);
    }

    @Test
    void getBranchById_notFound() {
        when(branchService.getBranchById(Mono.just(1)))
                .thenReturn(Mono.empty());
        webTestClient.post()
                .uri("/branches/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(branchRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllBranches_success() {
        when(branchService.getAllBranches()).thenReturn(Flux.just(branch));
        webTestClient.get()
                .uri("/branches")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BranchResponseDTO.class)
                .hasSize(1)
                .contains(branchResponse);
    }

    @Test
    void updateBranch_success() {
        when(branchService.updateBranch(Mono.just(any(Branch.class))))
                .thenReturn(Mono.just(branch));
        webTestClient.put()
                .uri("/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(branchRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchResponseDTO.class)
                .isEqualTo(branchResponse);
    }

    @Test
    void deleteBranch_success() {
        when(branchService.deleteBranch(Mono.just(1))).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/branches")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteBranch_notFound() {
        when(branchService.deleteBranch(Mono.just(1))).thenReturn(Mono.error(new RuntimeException("Branch not found")));
        webTestClient.delete()
                .uri("/branches")
                .exchange()
                .expectStatus().isNotFound();
    }*/
}
