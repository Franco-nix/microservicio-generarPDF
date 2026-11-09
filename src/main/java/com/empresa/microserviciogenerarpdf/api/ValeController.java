package com.empresa.microserviciogenerarpdf.api;

import com.empresa.microserviciogenerarpdf.application.microserviciogenerarpdfervice;
import com.empresa.microserviciogenerarpdf.domain.model.ArchivoPdf;
import com.empresa.microserviciogenerarpdf.domain.model.EstadoVale;
import com.empresa.microserviciogenerarpdf.domain.model.FiltroVale;
import com.empresa.microserviciogenerarpdf.domain.model.ValeResumen;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vales")
@Validated
public class ValeController {

    private final ValeService valeService;

    public ValeController(ValeService valeService) {
        this.valeService = valeService;
    }

    @GetMapping
    public List<ValeResumen> buscar(
            @RequestParam(required = false) EstadoVale estado,
            @RequestParam(required = false) @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos") String dni,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int tamano) {
        return valeService.buscar(new FiltroVale(estado, dni, pagina, tamano));
    }

    @GetMapping("/{codigo}/pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable String codigo) {
        ArchivoPdf pdf = valeService.generarPdf(codigo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(pdf.nombre()).build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.contenido());
    }
}
