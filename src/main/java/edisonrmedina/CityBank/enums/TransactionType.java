package edisonrmedina.CityBank.enums;

public enum TransactionType {
    DEPOSIT_BRANCH,       // Depósito desde sucursal
    DEPOSIT_ATM,          // Depósito desde cajero
    DEPOSIT_ACCOUNT,      // Depósito desde otra cuenta
    PURCHASE_PHYSICAL,    // Compra en establecimiento físico
    PURCHASE_ONLINE,      // Compra en página web
    WITHDRAW_ATM,          // Retiro en cajero
    DEPOSIT_OUT
}
