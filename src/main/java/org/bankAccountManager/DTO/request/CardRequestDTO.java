package org.bankAccountManager.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequestDTO {
    private int id;
    @NotNull(message = "Card's number is required")
    @NotBlank(message = "Card's number cannot be blank")
    @Size(min = 13, max = 19, message = "Card number must be between 13 and 19 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String cardNumber;
    @Size(max = 50, message = "Card type can be a maximum of 50 characters")
    private String cardType;
    @NotNull(message = "Card's expiration date is required")
    private LocalDateTime expirationDate;
    @NotNull(message = "CVV is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV must be 3 or 4 digits")
    private String cvv;
}
