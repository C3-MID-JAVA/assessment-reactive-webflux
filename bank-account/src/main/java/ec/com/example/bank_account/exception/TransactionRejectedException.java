package ec.com.example.bank_account.exception;

public class TransactionRejectedException  extends RuntimeException {
    public TransactionRejectedException(String message) {
        super(message);
    }
}