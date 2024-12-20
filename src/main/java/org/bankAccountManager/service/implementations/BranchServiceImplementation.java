package org.bankAccountManager.service.implementations;

import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.repository.BranchRepository;
import org.bankAccountManager.service.interfaces.BranchService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BranchServiceImplementation implements BranchService {

    private final BranchRepository branchRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public BranchServiceImplementation(BranchRepository branchRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.branchRepository = branchRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Branch> createBranch(Mono<Branch> branch) {
        return branch.flatMap(bEnt ->
                branchRepository.existsById(bEnt.getId()).flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException("Account already exists"));
                    return reactiveMongoTemplate.save(bEnt);
                })
        );
    }

    @Override
    public Mono<Branch> getBranchById(Mono<Integer> id) {
        return id.flatMap(branchRepository::findBranchById);
    }

    @Override
    public Mono<Branch> getBranchByName(Mono<String> name) {
        return name.flatMap(branchRepository::findBranchByName);
    }

    @Override
    public Flux<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public Mono<Branch> updateBranch(Mono<Branch> branch) {
        return branch.flatMap(bEnt ->
                reactiveMongoTemplate.findAndModify(
                                Query.query(Criteria.where("id").is(bEnt.getId())),
                                new Update()
                                        .set("name", bEnt.getName())
                                        .set("address", bEnt.getAddress())
                                        .set("phone", bEnt.getPhone()),
                                Branch.class)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found"))));
    }

    @Override
    public Mono<Void> deleteBranch(Mono<Integer> id) {
        return id.flatMap(branchRepository::deleteById);
    }
}
