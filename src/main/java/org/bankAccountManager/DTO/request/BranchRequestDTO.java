package org.bankAccountManager.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchRequestDTO {
    private int id;
    @NotNull(message = "Branch name is required")
    @NotBlank(message = "Branch name cannot be blank")
    @Size(min = 3, max = 100, message = "Branch name must be between 3 and 100 characters")
    private String name;
    @NotNull(message = "Branch address is required")
    @NotBlank(message = "Branch address cannot be blank")
    @Size(min = 5, max = 200, message = "Branch address must be between 5 and 200 characters")
    private String address;
    @NotNull(message = "Branch phone is required")
    @NotBlank(message = "Branch phone cannot be blank")
    private String phone;
}
