package com.empresa.microserviciogenerarpdf.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ValeDetalle(
        String codigoVale,
        EstadoVale estado,
        LocalDateTime fechaEmision,
        String zonal,
        String localidad,
        String proveedorRuc,
        String proveedorRazonSocial,
        String nroContrato,
        String idSuministro,
        BigDecimal cantidadEmitida,
        BigDecimal precioContrato,
        String trabajadorDni,
        String trabajadorNombre,
        String centroCosto,
        String unidad,
        String placa,
        LocalDateTime fechaAutorizacion,
        LocalDateTime fechaConsumo,
        BigDecimal cantidadConsumida,
        BigDecimal precioReal,
        BigDecimal importe,
        String ticket,
        LocalDateTime fechaAnulacion,
        String observacionAnulacion,
        String observacion) {
}
