package es.cuenta_bancaria_webflux.exception;

public class CuentaNoEncontradaException extends RuntimeException{
    public CuentaNoEncontradaException(String message){
        super(message);
    }
}
