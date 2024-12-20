package es.cuenta_bancaria_webflux.exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();

        // Manejo de errores de validación (MethodArgumentNotValidException)
        if (throwable instanceof MethodArgumentNotValidException) {
            try {
                return handleValidationError((MethodArgumentNotValidException) throwable, exchange, bufferFactory);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        // Manejo de excepciones personalizadas
        if (throwable instanceof CuentaNoEncontradaException) {
            return handleCuentaNoEncontrada((CuentaNoEncontradaException) throwable, exchange, bufferFactory);
        }

        if (throwable instanceof SaldoInsuficienteException) {
            return handleSaldoInsuficiente((SaldoInsuficienteException) throwable, exchange, bufferFactory);
        }

        // Manejo de errores generales
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer dataBuffer = bufferFactory.wrap("Unknown error".getBytes());
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    // Manejo específico de errores de validación
    private Mono<Void> handleValidationError(MethodArgumentNotValidException ex, ServerWebExchange exchange, DataBufferFactory bufferFactory) throws JsonProcessingException {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        // Capturar errores de validación
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // Configuración de la respuesta con los mensajes de error
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(errors));
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    // Manejo específico de la excepción CuentaNoEncontradaException
    private Mono<Void> handleCuentaNoEncontrada(CuentaNoEncontradaException ex, ServerWebExchange exchange, DataBufferFactory bufferFactory) {
        String errorMessage = ex.getMessage();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer dataBuffer = bufferFactory.wrap(errorMessage.getBytes());
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    // Manejo específico de la excepción SaldoInsuficienteException
    private Mono<Void> handleSaldoInsuficiente(SaldoInsuficienteException ex, ServerWebExchange exchange, DataBufferFactory bufferFactory) {
        String errorMessage = ex.getMessage();
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer dataBuffer = bufferFactory.wrap(errorMessage.getBytes());
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}