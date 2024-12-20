package ec.com.example.bank_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CardResponseDTO implements Serializable {

    private String holderName;
    private BigDecimal limitation;
    private String cvcCode;
    private Date expirationDate;
    private String status;
    private Mono<AccountResponseDTO> account;

}