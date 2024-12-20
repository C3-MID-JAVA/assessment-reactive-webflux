package ec.com.example.bank_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO implements Serializable {

    private String firstName;
    private String lastName;
    private String ci;
    private String email;
    private String status;

}
