package edisonrmedina.CityBank.service.impl;

import edisonrmedina.CityBank.dto.BankAccountDTO;
import edisonrmedina.CityBank.dto.CreateBankAccountDTO;
import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.mapper.Mapper;
import edisonrmedina.CityBank.repository.BankAccountRepository;
import edisonrmedina.CityBank.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@ComponentScan
public class BankAccountServiceImp implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImp(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }


    /**
     * Obtiene una cuenta bancaria por ID.
     *
     * @param id Identificador de la cuenta bancaria.
     * @return Un Mono con la cuenta bancaria, o vacío si no se encuentra.
     */
    public Mono<BankAccount> getBankAccount(String id) {
        return bankAccountRepository.findById(id);
    }

    public Mono<Void> updateBankAccount(BankAccount account) {
        if (account == null || account.getId() == null) {
            return Mono.error(new IllegalArgumentException("La cuenta bancaria no puede ser nula o carecer de ID"));
        }

        return Mono.fromRunnable(() -> bankAccountRepository.save(account)); // Realiza la operación de guardado de manera reactiva
    }


    public Mono<BankAccountDTO> register(Mono<CreateBankAccountDTO> createBankAccountDTOMono) {
        Logger logger = LoggerFactory.getLogger(getClass());

        return createBankAccountDTOMono
                .doOnSubscribe(subscription -> logger.info("Iniciando el registro de la cuenta bancaria"))
                .flatMap(this::validateBankAccountDTO)
                .flatMap(this::saveBankAccount)
                .map(this::convertToDTO)
                .doOnTerminate(() -> logger.info("Finalizó el proceso de registro de la cuenta bancaria"))
                .onErrorResume(e -> {
                    logger.error("Error inesperado al registrar la cuenta bancaria", e);
                    return Mono.error(new RuntimeException("Error inesperado al registrar la cuenta bancaria"));
                });
    }

    private Mono<CreateBankAccountDTO> validateBankAccountDTO(CreateBankAccountDTO dto) {
        Logger logger = LoggerFactory.getLogger(getClass());

        // Validación de los datos del DTO
        if (dto == null) {
            logger.error("El DTO de la cuenta bancaria es nulo");
            return Mono.error(new IllegalArgumentException("El DTO de la cuenta bancaria no puede ser nulo"));
        }
        logger.debug("DTO recibido: {}", dto);

        if (dto.getAccountHolder() == null || dto.getAccountHolder().isEmpty()) {
            logger.error("El titular de la cuenta está vacío o nulo");
            return Mono.error(new IllegalArgumentException("El titular de la cuenta no puede estar vacío"));
        }

        if (dto.getBalance() == null || dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            logger.error("El saldo es negativo o nulo");
            return Mono.error(new IllegalArgumentException("El saldo no puede ser negativo"));
        }

        return Mono.just(dto);
    }

    private Mono<BankAccount> saveBankAccount(CreateBankAccountDTO dto) {
        Logger logger = LoggerFactory.getLogger(getClass());

        // Convertir el DTO en un objeto BankAccount
        BankAccount bankAccount = new BankAccount(dto.getAccountHolder(), dto.getBalance());
        logger.debug("BankAccount creado: {}", bankAccount);

        // Guardar el objeto BankAccount de manera reactiva
        logger.info("Iniciando el proceso de guardar la cuenta bancaria...");

        // Aquí se elimina el uso de block() y se maneja todo de manera reactiva
        return Mono.defer(() -> {
                    logger.info("Guardando la cuenta bancaria en el repositorio...");

                    // Guardar en el repositorio de manera reactiva
                    return bankAccountRepository.save(bankAccount)
                            .doOnSuccess(savedBankAccount -> logger.debug("Cuenta bancaria guardada: {}", savedBankAccount))
                            .doOnError(error -> logger.error("Error al guardar la cuenta bancaria", error));
                })
                .doOnTerminate(() -> logger.info("Proceso de guardar la cuenta bancaria finalizado"));
    }

    private BankAccountDTO convertToDTO(BankAccount savedBankAccount) {
        Logger logger = LoggerFactory.getLogger(getClass());

        // Convertimos el BankAccount guardado en BankAccountDTO
        BankAccountDTO bankAccountDTO = Mapper.bankAccountToDTO(savedBankAccount);
        logger.debug("BankAccountDTO convertido: {}", bankAccountDTO);

        return bankAccountDTO;
    }


}
