package ec.com.example.bank_account.service.impl;

import ec.com.example.bank_account.dto.TypeTransactionRequestDTO;
import ec.com.example.bank_account.dto.TypeTransactionResponseDTO;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.TypeTransactionMapper;
import ec.com.example.bank_account.repository.TypeTransactionRepository;
import ec.com.example.bank_account.service.TypeTransactionService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TypeTransactionServiceImpl implements TypeTransactionService {
    private final TypeTransactionRepository typeTransactionRepository;
    private final TypeTransactionMapper typeTransactionMapper;

    public TypeTransactionServiceImpl(TypeTransactionRepository typeTransactionRepository, TypeTransactionMapper typeTransactionMapper) {
        this.typeTransactionRepository = typeTransactionRepository;
        this.typeTransactionMapper = typeTransactionMapper;
    }

    @Override
    public Mono<TypeTransactionResponseDTO> createTypeTransaction(TypeTransactionRequestDTO typeTransaction) {
        return Mono.just(typeTransaction)
                .map(typeTransactionMapper::mapToEntity)
                .flatMap(typeTransactionRepository::save)
                .map(typeTransactionMapper::mapToDTO);
    }

    @Override
    public Flux<TypeTransactionResponseDTO> getAllTypeTransactions() {
        return typeTransactionRepository.findAll()
                .map(typeTransactionMapper::mapToDTO)
                .switchIfEmpty(Flux.error(new EmptyCollectionException("No typesTransaction records found.")));
    }
}