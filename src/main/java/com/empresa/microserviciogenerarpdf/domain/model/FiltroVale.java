package com.empresa.microserviciogenerarpdf.domain.model;

public record FiltroVale(EstadoVale estado, String dniTrabajador, int pagina, int tamano) {

    public Integer codigoEstado() {
        return estado == null ? null : estado.getCodigo();
    }

    public int offset() {
        return pagina * tamano;
    }
}
