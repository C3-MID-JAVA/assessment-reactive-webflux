package org.bankAccountManager.DTO.response;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponseDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
}
