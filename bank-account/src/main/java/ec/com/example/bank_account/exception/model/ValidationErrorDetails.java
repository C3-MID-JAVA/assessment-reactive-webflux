package ec.com.example.bank_account.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorDetails {

    private Date timestamp;
    private String message;
    private Map<String, String> fieldErrors;

}