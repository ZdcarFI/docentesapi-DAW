package com.example.docentesapi.exception;


public class DocenteNotFoundException extends RuntimeException {


    public DocenteNotFoundException(String mensaje) {
        super(mensaje);
    }

}