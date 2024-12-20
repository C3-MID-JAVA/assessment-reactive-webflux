package es.cuenta_bancaria_webflux.mapper;

import es.cuenta_bancaria_webflux.dto.AccountDTO;
import es.cuenta_bancaria_webflux.model.Account;
import es.cuenta_bancaria_webflux.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AccountMapper {

    // Convertir de Cuenta a CuentaDTO
    public AccountDTO toDto(Account cuenta) {
        AccountDTO dto = new AccountDTO();
        dto.setIdCuenta(cuenta.getIdCuenta());
        dto.setTitular(cuenta.getTitular());
        dto.setSaldo(cuenta.getSaldo());
        // Convertir lista de transacciones a lista de IDs
        dto.setTransacciones(cuenta.getTransacciones()
                .stream()
                .map(Transaction::getIdTransaccion)
                .toList());
        return dto;
    }

    // Convertir de CuentaDTO a Cuenta
    public  Account toEntity(AccountDTO accountDTO) {
        Account cuenta = new Account();
        cuenta.setIdCuenta(accountDTO.getIdCuenta());
        cuenta.setTitular(accountDTO.getTitular());
        cuenta.setSaldo(accountDTO.getSaldo());
        // Dejar la lista de transacciones vacía (se puede manejar en el servicio)
        cuenta.setTransacciones(new ArrayList<>());
        return cuenta;
    }
}
