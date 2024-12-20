package org.bankAccountManager.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Branch")
public class Branch {
    @Id
    private int id;
    private String name;
    private String address;
    private String phone;
}
