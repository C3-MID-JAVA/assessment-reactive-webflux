package org.bankAccountManager.service.interfaces;


import org.bankAccountManager.entity.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchService {
    Mono<Branch> createBranch(Mono<Branch> branch);

    Mono<Branch> getBranchById(Mono<Integer> id);

    Mono<Branch> getBranchByName(Mono<String> name);

    Flux<Branch> getAllBranches();

    Mono<Branch> updateBranch(Mono<Branch> branch);

    Mono<Void> deleteBranch(Mono<Integer> id);
}
