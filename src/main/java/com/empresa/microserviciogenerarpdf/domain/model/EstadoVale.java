package com.empresa.microserviciogenerarpdf.domain.model;

import java.util.Arrays

public enum EstadoVale {
    EMITIDO(0), AUTORIZADO(1), CONSUMIDO(2), USADO(3), ANULADO(4);

    private final int codigo;

    EstadoVale(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static EstadoVale fromCodigo(int codigo) {
        return Arrays.stream(values())
            .filter(e -> e.codigo == codigo)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Estado del vale desconodico: " + codigo));
    }
}
