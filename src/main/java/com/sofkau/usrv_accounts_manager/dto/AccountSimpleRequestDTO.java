package com.sofkau.usrv_accounts_manager.dto;

import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request body for getting all cards by account number")
public class AccountSimpleRequestDTO {


    @NotNull(message = "accountNumber" + ConstansTrType.NOT_NULL_FIELD)
    private String accountNumber;


}
