package ec.com.example.bank_account.exception;

public class RecordNotFoundException  extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}