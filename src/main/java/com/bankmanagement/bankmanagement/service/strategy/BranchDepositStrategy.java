package com.bankmanagement.bankmanagement.service.strategy;

import org.springframework.stereotype.Component;

@Component
public class BranchDepositStrategy implements TransactionStrategy{
    @Override
    public double calculateFee() {
        return 0;
    }

    @Override
    public double calculateBalance(double balance, double amount) {
        double fee = calculateFee();
        return balance + (amount - fee);
    }
}
