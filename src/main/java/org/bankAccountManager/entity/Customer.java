package org.bankAccountManager.entity;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Customer")
public class Customer {
    @Id
    private int id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
    private String address;
    @DBRef
    private List<Account> accounts;
}