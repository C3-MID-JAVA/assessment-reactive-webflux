package ec.com.example.bank_account.util.process;
import ec.com.example.bank_account.entity.Transaction;
import ec.com.example.bank_account.entity.TypeTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class BalanceCalculatorImplTest {

    private final BalanceCalculatorImpl balanceCalculator = new BalanceCalculatorImpl();

    @Test
    public void testCalculate_WithDiscount() {
        Transaction transaction = mock(Transaction.class);
        TypeTransaction typeTransaction = mock(TypeTransaction.class);

        when(transaction.getValue()).thenReturn(new BigDecimal("100"));
        when(typeTransaction.getValue()).thenReturn(new BigDecimal("10"));
        when(transaction.getTypeTransaction()).thenReturn(typeTransaction);
        when(typeTransaction.getDiscount()).thenReturn(true);

        BigDecimal currentBalance = new BigDecimal("500");

        BigDecimal result = balanceCalculator.calculate(transaction, currentBalance);

        assertEquals(new BigDecimal("390"), result);
    }

    @Test
    public void testCalculate_WithoutDiscount() {
        Transaction transaction = mock(Transaction.class);
        TypeTransaction typeTransaction = mock(TypeTransaction.class);

        when(transaction.getValue()).thenReturn(new BigDecimal("100"));
        when(typeTransaction.getValue()).thenReturn(new BigDecimal("0"));
        when(transaction.getTypeTransaction()).thenReturn(typeTransaction);
        when(typeTransaction.getDiscount()).thenReturn(false);

        BigDecimal currentBalance = new BigDecimal("500");

        BigDecimal result = balanceCalculator.calculate(transaction, currentBalance);

        assertEquals(new BigDecimal("600"), result);
    }
}
