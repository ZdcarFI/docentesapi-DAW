package com.example.docentesapi.exception;


import lombok.Getter;

@Getter
public class InvalidDateException extends RuntimeException {

    private final String field;
    private final Object value;


    public InvalidDateException(String field, Object value, String message) {
        super(message);
        this.field = field;
        this.value = value;
    }

}