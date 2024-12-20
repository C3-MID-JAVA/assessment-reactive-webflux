package es.cuenta_bancaria_webflux.model;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account")
public class Account {

    @Id
    private String idCuenta;

    @NotNull
    @Size(max = 100)
    //@Column(nullable = false, length = 100)
    private String titular;

    //@Column(nullable = false)
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal saldo;

    //@DBRef
    
    private List<Transaction> transacciones; // Relación referenciada

}
