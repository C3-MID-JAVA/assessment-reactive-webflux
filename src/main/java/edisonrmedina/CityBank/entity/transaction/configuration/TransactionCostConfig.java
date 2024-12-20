package edisonrmedina.CityBank.entity.transaction.configuration;

import edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy.*;
import edisonrmedina.CityBank.enums.TransactionType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TransactionCostConfig {
    @Bean
    public Map<TransactionType, TransactionCostStrategy> costStrategies() {
        return Map.of(
                TransactionType.DEPOSIT_BRANCH, new DepositBranchCostStrategy(),
                TransactionType.DEPOSIT_ATM, new DepositAtmCostStrategy(),
                TransactionType.DEPOSIT_ACCOUNT, new DepositAccountCostStrategy(),
                TransactionType.PURCHASE_PHYSICAL, new PurchasePhysicalCostStrategy(),
                TransactionType.PURCHASE_ONLINE, new PurchaseWebCostStrategy(),
                TransactionType.WITHDRAW_ATM, new WithdrawAtmCostStrategy()
        );
    }
}
