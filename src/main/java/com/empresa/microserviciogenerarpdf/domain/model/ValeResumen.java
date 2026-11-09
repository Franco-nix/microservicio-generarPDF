package com.empresa.microserviciogenerarpdf.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ValeResumen (
        String codigoVale,
        LocalDateTime fechaEmision,
        String trabajador,
        String proveedor,
        BigDecimal cantidadEmitida,
        EstadoVale estado) {
}
