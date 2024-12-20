package ec.com.example.bank_account.mapper;

import ec.com.example.bank_account.dto.TypeAccountRequestDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.entity.TypeAccount;
import org.springframework.stereotype.Component;

@Component
public class TypeAccountMapper {

    public TypeAccount mapToEntity(TypeAccountRequestDTO typeAccountRequestDTO) {
        if (typeAccountRequestDTO == null) {
            return null;
        }
        TypeAccount typeAccount = new TypeAccount();
        typeAccount.setType(typeAccountRequestDTO.getType());
        typeAccount.setDescription(typeAccountRequestDTO.getDescription());
        typeAccount.setStatus(typeAccountRequestDTO.getStatus());
        return typeAccount;
    }

    public TypeAccountResponseDTO mapToDTO(TypeAccount typeAccount) {
        if (typeAccount == null) {
            return null;
        }
        return new TypeAccountResponseDTO(
                typeAccount.getType(),
                typeAccount.getDescription(),
                typeAccount.getStatus()
        );
    }

}
