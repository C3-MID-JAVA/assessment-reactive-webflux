package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.UserRequestDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.entity.User;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.UserMapper;
import ec.com.example.bank_account.repository.UserRepository;
import ec.com.example.bank_account.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setCi("1310000000");
        user.setEmail("diego.loor@sofka.com.co");
        user.setFirstName("Diego");
        user.setLastName("Loor");
        user.setPassword("diego.loor@sofka.com.co");
        user.setStatus("ACTIVE");

        userRequestDTO = new UserRequestDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co","Diego123.", "ACTIVE");

        userResponseDTO = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");
    }

    @Test
    public void createUser_ShouldReturnCreatedEntity_WhenUserIsCreatedSuccessfully() {
        when(userMapper.mapToEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        when(userMapper.mapToDTO(user)).thenReturn(userResponseDTO);

        Mono<UserResponseDTO> result = userService.createUser(userRequestDTO);

        StepVerifier.create(result)
                .expectNext(userResponseDTO)
                .verifyComplete();

        verify(userRepository).save(user);
        verify(userMapper).mapToEntity(userRequestDTO);
        verify(userMapper).mapToDTO(user);
    }

    @Test
    public void testGetAllUsers_WhenUsersExist() {
        when(userRepository.findAll()).thenReturn(Flux.just(user));
        when(userMapper.mapToDTO(user)).thenReturn(userResponseDTO);

        Flux<UserResponseDTO> result = userService.getAllUsers();

        StepVerifier.create(result)
                .expectNext(userResponseDTO)
                .verifyComplete();

        verify(userRepository).findAll();
        verify(userMapper).mapToDTO(user);
    }

    @Test
    public void testGetAllUsers_WhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Flux.empty());

        Flux<UserResponseDTO> result = userService.getAllUsers();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EmptyCollectionException &&
                        throwable.getMessage().equals("No users records found."))
                .verify();

        verify(userRepository).findAll();
        verifyNoInteractions(userMapper);
    }
}