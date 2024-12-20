package com.reactivo.banco.service;

import com.reactivo.banco.model.dto.TransactionInDTO;
import com.reactivo.banco.model.dto.TransactionOutDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface TransactionService {

    Mono<TransactionOutDTO> makeBranchDeposit(TransactionInDTO movimientoInDTO);

    Mono<TransactionOutDTO> makeATMDeposit(TransactionInDTO movimientoInDTO);

    Mono<TransactionOutDTO> makeDepositToAnotherAccount(TransactionInDTO movimientoInDTO);

    Mono<TransactionOutDTO> makePhysicalPurchase(TransactionInDTO movimientoInDTO);

    Mono<TransactionOutDTO> makeOnlinePurchase(TransactionInDTO movimientoInDTO);

    Mono<TransactionOutDTO> makeATMWithdrawal(TransactionInDTO movimientoInDTO);

}
