package com.bankmanagement.bankmanagement.service.strategy;

public interface TransactionStrategy {
    double calculateFee();
    double calculateBalance(double balance, double amount);
}
