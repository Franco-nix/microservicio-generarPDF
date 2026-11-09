package com.empresa.microserviciogenerarpdf.domain.exception;

public class ValeNoEncontradoException extends RuntimeException {
    public ValeNoEncontradoException(String codigoVale) {
        super("No existe el vale " + codigoVale);
    }
} 
