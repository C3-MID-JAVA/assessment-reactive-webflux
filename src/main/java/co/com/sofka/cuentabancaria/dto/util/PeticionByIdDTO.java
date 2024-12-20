package co.com.sofka.cuentabancaria.dto.util;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PeticionByIdDTO {
    @NotNull
    private String cuentaId;
}
