package org.bankAccountManager.DTO.response;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
    private int id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
    private String address;
    private List<AccountResponseDTO> accounts;
}
