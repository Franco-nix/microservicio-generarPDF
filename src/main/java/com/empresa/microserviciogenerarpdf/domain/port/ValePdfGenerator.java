package com.empresa.microserviciogenerarpdf.domain.port;

import com.empresa.microserviciogenerarpdf.domain.model.ValeDetalle;

public interface ValePdfGenerator {
    byte[] generar(ValeDetalle vale);
}
