package ec.com.example.bank_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TypeAccountResponseDTO implements Serializable {

    private String type;
    private String description;
    private String status;

}
