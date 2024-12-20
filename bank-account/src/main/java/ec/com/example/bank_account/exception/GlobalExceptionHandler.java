package ec.com.example.bank_account.exception;

import ec.com.example.bank_account.exception.model.ErrorDetails;
import ec.com.example.bank_account.exception.model.ValidationErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyCollectionException.class)
    public ResponseEntity<Mono<ErrorDetails>> handleEmptyCollectionException(EmptyCollectionException ex) {
        ErrorDetails errorDetails = new ErrorDetails(404, ex.getMessage(), new Date());
        return new ResponseEntity<>(Mono.just(errorDetails), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionRejectedException.class)
    public ResponseEntity<ErrorDetails> handleTransactionRejectedException(TransactionRejectedException ex) {
        ErrorDetails errorDetails = new ErrorDetails(400, ex.getMessage(), new Date());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleRecordNotFoundException(RecordNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(404, ex.getMessage(), new Date());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorDetails> handleInternalServerException(InternalServerException ex) {
        ErrorDetails errorDetails = new ErrorDetails(500, ex.getMessage(), new Date());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDetails> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        ValidationErrorDetails errorDetails = new ValidationErrorDetails(
                new Date(),
                "Validation failed for one or more fields.",
                fieldErrors
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}