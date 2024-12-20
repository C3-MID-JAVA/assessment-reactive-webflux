package es.cuenta_bancaria_webflux.mapper;
import es.cuenta_bancaria_webflux.dto.TransactionDTO;
import es.cuenta_bancaria_webflux.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    //Convertir de Transaccion a transaccion dto
    public TransactionDTO toDto(Transaction transaction){
        if (transaction == null) {
            return null; // Evitar NullPointerException si la transacción es nula
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setIdCuenta(transaction.getIdCuenta());
        dto.setCosto(transaction.getCosto());
        dto.setMonto(transaction.getMonto());
        dto.setTipo(transaction.getTipo());
        dto.setIdTransaccion(transaction.getIdTransaccion());
        return dto;
    }

    //Convertir de transactionDTO a Transaction
    public  Transaction toEntity(TransactionDTO dto) {
        Transaction transaccion = new Transaction();
        transaccion.setIdTransaccion(dto.getIdTransaccion());
        transaccion.setMonto(dto.getMonto());
        transaccion.setTipo(dto.getTipo());
        transaccion.setCosto(dto.getCosto());
        transaccion.setIdCuenta(dto.getIdCuenta());
        return transaccion;
    }

}
