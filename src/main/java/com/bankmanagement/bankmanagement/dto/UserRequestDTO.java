package com.bankmanagement.bankmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank(message = "Document ID cannot be blank")
    @Size(min = 3, max = 15)
    private String documentId;
}
