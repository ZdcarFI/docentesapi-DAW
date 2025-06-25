package com.example.docentesapi.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String mensaje) {
        super(mensaje);
    }

}