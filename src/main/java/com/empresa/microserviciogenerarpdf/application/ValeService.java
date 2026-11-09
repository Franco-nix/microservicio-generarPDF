package com.empresa.microserviciogenerarpdf.application;

import com.empresa.microserviciogenerarpdf.domain.exception.ValeNoEncontradoException;
import com.empresa.microserviciogenerarpdf.domain.model.ArchivoPdf;
import com.empresa.microserviciogenerarpdf.domain.model.FiltroVale;
import com.empresa.microserviciogenerarpdf.domain.model.ValeDetalle;
import com.empresa.microserviciogenerarpdf.domain.model.ValeResumen;
import com.empresa.microserviciogenerarpdf.domain.port.ValePdfGenerator;
import com.empresa.microserviciogenerarpdf.domain.port.ValeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValeService {

    private final ValeRepository valeRepository;
    private final ValePdfGenerator pdfGenerator;

    public ValeService(ValeRepository valeRepository, ValePdfGenerator pdfGenerator) {
        this.valeRepository = valeRepository;
        this.pdfGenerator = pdfGenerator;
    }

    public List<ValeResumen> buscar(FiltroVale filtro) {
        return valeRepository.buscar(filtro);
    }

    public ArchivoPdf generarPdf(String codigoVale) {
        ValeDetalle vale = valeRepository.buscarDetallePorCodigo(codigoVale)
                .orElseThrow(() -> new ValeNoEncontradoException(codigoVale));
        byte[] contenido = pdfGenerator.generar(vale);
        return new ArchivoPdf("vale-" + codigoVale + ".pdf", contenido);
    }
}
