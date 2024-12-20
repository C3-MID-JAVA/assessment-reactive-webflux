package com.sofka.bank.entity;

public enum TransactionType {
    DEPOSIT_ATM(2.0),
    DEPOSIT_OTHER_ACCOUNT(1.5),
    WITHDRAW_ATM(1.0),
    ONLINE_PURCHASE(5.0),
    BRANCH_DEPOSIT(0.0),
    ONSITE_CARD_PURCHASE(0.0);

    private final double fee;

    TransactionType(double fee) {
        this.fee = fee;
    }

    public double getFee() {
        return fee;
    }

    public static TransactionType fromString(String type) {
        for (TransactionType t : TransactionType.values()) {
            if (t.name().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid transaction type: " + type);
    }
}