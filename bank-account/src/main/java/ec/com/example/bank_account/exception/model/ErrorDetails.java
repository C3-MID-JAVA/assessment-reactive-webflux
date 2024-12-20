package ec.com.example.bank_account.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {

    private int status;
    private String message;
    private Date timestamp;

}