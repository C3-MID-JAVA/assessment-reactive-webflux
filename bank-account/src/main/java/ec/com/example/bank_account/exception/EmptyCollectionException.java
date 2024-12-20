package ec.com.example.bank_account.exception;

public class EmptyCollectionException extends RuntimeException {
    public EmptyCollectionException(String message) {
        super(message);
    }
}
