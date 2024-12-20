package co.com.sofka.cuentabancaria.model;

import co.com.sofka.cuentabancaria.model.enums.TipoTransaccion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transacciones")
@Getter
@Setter
@NoArgsConstructor
public class Transaccion {
    @Id
    private String id;

    private BigDecimal monto;

    private BigDecimal costoTransaccion;

    private LocalDateTime fecha;

    private TipoTransaccion tipo;

    private String cuentaId;

    public Transaccion(BigDecimal monto, BigDecimal costoTransaccion, LocalDateTime fecha, TipoTransaccion tipo, String cuentaId) {
        this.monto = monto;
        this.costoTransaccion = costoTransaccion;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cuentaId = cuentaId;
    }

}
