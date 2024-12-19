package com.bankmanagement.bankmanagement.service.strategy;

import org.springframework.stereotype.Component;

@Component("aTMDepositStrategy")
public class ATMDepositStrategy implements TransactionStrategy {
    @Override
    public double calculateFee() {
        return 2;
    }

    @Override
    public double calculateBalance(double balance, double amount) {
        double fee = calculateFee();
        return balance + (amount - fee);
    }
}
