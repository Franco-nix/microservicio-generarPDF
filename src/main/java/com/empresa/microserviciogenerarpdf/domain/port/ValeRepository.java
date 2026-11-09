package com.empresa.microserviciogenerarpdf.domain.port;

import com.empresa.microserviciogenerarpdf.domain.model.FiltroVale;
import com.empresa.microserviciogenerarpdf.domain.model.ValeDetalle;
import com.empresa.microserviciogenerarpdf.domain.model.ValeResumen;
import java.util.List;
import java.util.Optical;

public interface ValeRepository {
    List<ValeResumen> buscar(FiltroVale filtro);
    Optional<ValeDetalle> buscarDetallePorCodigo(String codigoVale);
}

