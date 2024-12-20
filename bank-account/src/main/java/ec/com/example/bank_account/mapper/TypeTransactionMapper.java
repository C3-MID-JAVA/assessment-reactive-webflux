package ec.com.example.bank_account.mapper;

import ec.com.example.bank_account.dto.TypeTransactionRequestDTO;
import ec.com.example.bank_account.dto.TypeTransactionResponseDTO;
import ec.com.example.bank_account.entity.TypeTransaction;
import org.springframework.stereotype.Component;

@Component
public class TypeTransactionMapper {

    public TypeTransaction mapToEntity(TypeTransactionRequestDTO typeTransactionRequestDTO) {
        if (typeTransactionRequestDTO == null) {
            return null;
        }
        TypeTransaction typeTransaction = new TypeTransaction();
        typeTransaction.setType(typeTransactionRequestDTO.getType());
        typeTransaction.setDescription(typeTransactionRequestDTO.getDescription());
        typeTransaction.setValue(typeTransactionRequestDTO.getValue());
        typeTransaction.setDiscount(typeTransactionRequestDTO.getDiscount());
        typeTransaction.setTransactionCost(typeTransactionRequestDTO.getTransactionCost());
        typeTransaction.setStatus(typeTransactionRequestDTO.getStatus());

        return typeTransaction;
    }

    public TypeTransactionResponseDTO mapToDTO(TypeTransaction typeTransaction) {
        if (typeTransaction == null) {
            return null;
        }
        return new TypeTransactionResponseDTO(
                typeTransaction.getType(),
                typeTransaction.getDescription(),
                typeTransaction.getValue(),
                typeTransaction.getDiscount(),
                typeTransaction.getDiscount(),
                typeTransaction.getStatus()
        );
    }

}
