package com.reactivo.banco.service.impl;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.mapper.AccountMapper;
import com.reactivo.banco.model.dto.AccountInDTO;
import com.reactivo.banco.model.dto.AccountOutDTO;
import com.reactivo.banco.model.entity.Account;
import com.reactivo.banco.repository.AccountRepository;
import com.reactivo.banco.repository.ClientRepository;
import com.reactivo.banco.service.AccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountImplService implements AccountService {

    private final AccountRepository cuentaRepository;
    private final ClientRepository clienteRepository;

    public AccountImplService(AccountRepository cuentaRepository, ClientRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Mono<AccountOutDTO> createAccount(AccountInDTO cuentaInDTO) {
        return clienteRepository.findById(String.valueOf(cuentaInDTO.getCustumerId()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente no encontrado con ID: " + cuentaInDTO.getCustumerId())))
                .flatMap(cliente -> {
                    Account cuenta = AccountMapper.toEntity(cuentaInDTO);
                    return cuentaRepository.save(cuenta);
                })
                .map(AccountMapper::toDTO);
    }

    @Override
    public Flux<AccountOutDTO> getAllAccounts() {
        return cuentaRepository.findAll()
                .map(AccountMapper::toDTO)
                .switchIfEmpty(Flux.error(new ResourceNotFoundException("No existen cuentas registradas.")));
    }

    @Override
    public Mono<AccountOutDTO> getAccountById(String id) {
        return cuentaRepository.findById(id)
                .map(AccountMapper::toDTO)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + id)));
    }

    @Override
    public Mono<AccountOutDTO> updateAccount(String id, AccountInDTO cuentaInDTO) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + id)))
                .flatMap(cuentaExistente -> {
                    cuentaExistente.setAccountNumber(cuentaInDTO.getAccountNumber());
                    cuentaExistente.setBalance(cuentaInDTO.getBalance());
                    return cuentaRepository.save(cuentaExistente);
                })
                .map(AccountMapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAccount(String id) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + id)))
                .flatMap(cuenta -> cuentaRepository.delete(cuenta));
    }
}
