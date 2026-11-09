package com.empresa.microserviciogenerarpdf.infrastructure.pdf;

import com.empresa.microserviciogenerarpdf.domain.exception.PdfGenerationException;
import com.empresa.microserviciogenerarpdf.domain.model.EstadoVale;
import com.empresa.microserviciogenerarpdf.domain.model.ValeDetalle;
import com.empresa.microserviciogenerarpdf.domain.port.ValePdfGenerator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class OpenPdfValeGenerator implements ValePdfGenerator {

    private static final Color COLOR_SECCION = new Color(0x1F, 0x4E, 0x79);
    private static final Color COLOR_ETIQUETA = new Color(0xEE, 0xEE, 0xEE);
    private static final Font FUENTE_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font FUENTE_SECCION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.NORMAL, Color.WHITE);
    private static final Font FUENTE_ETIQUETA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    private static final Font FUENTE_VALOR = FontFactory.getFont(FontFactory.HELVETICA, 9);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String SIN_DATO = "-";

    private record Campo(String etiqueta, String valor) { }

    @Override
    public byte[] generar(ValeDetalle vale) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
        try {
            PdfWriter.getInstance(documento, salida);
            documento.open();
            documento.add(titulo(vale));
            documento.add(seccion("Datos del vale", datosDelVale(vale)));
            documento.add(seccion("Trabajador y asignación", datosTrabajador(vale)));
            documento.add(seccion("Autorización y consumo", datosConsumo(vale)));
            if (vale.estado() == EstadoVale.ANULADO) {
                documento.add(seccion("Anulación", datosAnulacion(vale)));
            }
            documento.add(seccionTexto("Observaciones", vale.observacion()));
            documento.add(firmas());
            documento.close();
        } catch (DocumentException e) {
            throw new PdfGenerationException("No se pudo generar el PDF del vale " + vale.codigoVale(), e);
        }
        return salida.toByteArray();
    }

    // ---------- Contenido por sección ----------

    private List<Campo> datosDelVale(ValeDetalle v) {
        return List.of(
                campo("Estado", v.estado().name()),
                campo("Fecha de emisión", v.fechaEmision()),
                campo("Zonal", v.zonal()),
                campo("Localidad", v.localidad()),
                campo("Proveedor", v.proveedorRazonSocial()),
                campo("RUC", v.proveedorRuc()),
                campo("N° de contrato", v.nroContrato()),
                campo("N° de suministro", v.idSuministro()),
                campo("Cantidad emitida", v.cantidadEmitida()),
                campo("Precio contrato", v.precioContrato()));
    }

    private List<Campo> datosTrabajador(ValeDetalle v) {
        return List.of(
                campo("DNI", v.trabajadorDni()),
                campo("Trabajador", v.trabajadorNombre()),
                campo("Centro de costo", v.centroCosto()),
                campo("Unidad / maquinaria", v.unidad()),
                campo("Placa", v.placa()));
    }

    private List<Campo> datosConsumo(ValeDetalle v) {
        return List.of(
                campo("Fecha autorización", v.fechaAutorizacion()),
                campo("Fecha consumo", v.fechaConsumo()),
                campo("Cantidad consumida", v.cantidadConsumida()),
                campo("Precio real", v.precioReal()),
                campo("Importe", v.importe()),
                campo("Ticket", v.ticket()));
    }

    private List<Campo> datosAnulacion(ValeDetalle v) {
        return List.of(
                campo("Fecha anulación", v.fechaAnulacion()),
                campo("Motivo", v.observacionAnulacion()));
    }

    // ---------- Componentes visuales ----------

    private Paragraph titulo(ValeDetalle vale) {
        Paragraph titulo = new Paragraph("VALE N° " + vale.codigoVale(), FUENTE_TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        return titulo;
    }

    private PdfPTable seccion(String titulo, List<Campo> campos) {
        PdfPTable tabla = tablaBase(1.3f, 2f, 1.3f, 2f);
        tabla.addCell(celdaSeccion(titulo, 4));
        campos.forEach(c -> {
            tabla.addCell(celdaEtiqueta(c.etiqueta()));
            tabla.addCell(celdaValor(c.valor()));
        });
        if (campos.size() % 2 != 0) {
            tabla.addCell(celdaVacia());
            tabla.addCell(celdaVacia());
        }
        return tabla;
    }

    private PdfPTable seccionTexto(String titulo, String texto) {
        PdfPTable tabla = tablaBase(1f);
        tabla.addCell(celdaSeccion(titulo, 1));
        PdfPCell celda = celdaValor(valorOSinDato(texto));
        celda.setMinimumHeight(40);
        tabla.addCell(celda);
        return tabla;
    }

    private PdfPTable firmas() {
        PdfPTable tabla = tablaBase(1f, 0.3f, 1f);
        tabla.setSpacingBefore(60);
        tabla.addCell(celdaFirma("Firma del trabajador"));
        tabla.addCell(celdaVacia());
        tabla.addCell(celdaFirma("V°B° Jefatura"));
        return tabla;
    }

    private PdfPTable tablaBase(float... anchos) {
        PdfPTable tabla = new PdfPTable(anchos);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(12);
        return tabla;
    }

    private PdfPCell celdaSeccion(String texto, int columnas) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FUENTE_SECCION));
        celda.setColspan(columnas);
        celda.setBackgroundColor(COLOR_SECCION);
        celda.setPadding(5);
        return celda;
    }

    private PdfPCell celdaEtiqueta(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FUENTE_ETIQUETA));
        celda.setBackgroundColor(COLOR_ETIQUETA);
        celda.setPadding(4);
        return celda;
    }

    private PdfPCell celdaValor(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FUENTE_VALOR));
        celda.setPadding(4);
        return celda;
    }

    private PdfPCell celdaVacia() {
        PdfPCell celda = new PdfPCell();
        celda.setBorder(Rectangle.NO_BORDER);
        return celda;
    }

    private PdfPCell celdaFirma(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FUENTE_ETIQUETA));
        celda.setBorder(Rectangle.TOP);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPaddingTop(4);
        return celda;
    }

    // ---------- Formato de valores ----------

    private static Campo campo(String etiqueta, String valor) {
        return new Campo(etiqueta, valorOSinDato(valor));
    }

    private static Campo campo(String etiqueta, LocalDateTime valor) {
        return campo(etiqueta, valor == null ? null : valor.format(FORMATO_FECHA));
    }

    private static Campo campo(String etiqueta, BigDecimal valor) {
        return campo(etiqueta, valor == null ? null : String.format(Locale.US, "%,.2f", valor));
    }

    private static String valorOSinDato(String valor) {
        return valor == null || valor.isBlank() ? SIN_DATO : valor;
    }
}
