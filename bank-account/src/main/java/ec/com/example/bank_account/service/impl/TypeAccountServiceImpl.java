package ec.com.example.bank_account.service.impl;

import ec.com.example.bank_account.dto.TypeAccountRequestDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.TypeAccountMapper;
import ec.com.example.bank_account.repository.TypeAccountRepository;
import ec.com.example.bank_account.service.TypeAccountService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TypeAccountServiceImpl implements TypeAccountService {

    private final TypeAccountRepository typeAccountRepository;
    private final TypeAccountMapper typeAccountMapper;

    public TypeAccountServiceImpl(TypeAccountRepository typeAccountRepository, TypeAccountMapper typeAccountMapper) {
        this.typeAccountRepository = typeAccountRepository;
        this.typeAccountMapper = typeAccountMapper;
    }

    @Override
    public Mono<TypeAccountResponseDTO> createTypeAccount(TypeAccountRequestDTO typeAccount) {
        return Mono.just(typeAccount)
                .map(typeAccountMapper::mapToEntity)
                .flatMap(typeAccountRepository::save)
                .map(typeAccountMapper::mapToDTO);
    }

    @Override
    public Flux<TypeAccountResponseDTO> getAllTypeAccount() {
        return typeAccountRepository.findAll()
                .map(typeAccountMapper::mapToDTO)
                .switchIfEmpty(Flux.error(new EmptyCollectionException("No typesAccount records found.")));
    }
}