package com.sofkau.usrv_accounts_manager.Utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private Object details;

    public ErrorDetails(Date timestamp, String message, Object details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}