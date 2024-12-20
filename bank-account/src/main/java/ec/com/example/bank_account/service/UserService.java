package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.UserRequestDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserResponseDTO> createUser(UserRequestDTO user);

    Flux<UserResponseDTO> getAllUsers();

}