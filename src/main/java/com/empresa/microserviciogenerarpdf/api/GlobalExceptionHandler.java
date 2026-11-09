package com.empresa.microserviciogenerarpdf.api;

import com.empresa.microserviciogenerarpdf.domain.exception.PdfGenerationException;
import com.empresa.microserviciogenerarpdf.domain.exception.PdfGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValeNoEncontradoException.class)
    public ProblemDetail vale​NoEncontrado(ValeNoEncontradoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ProblemDetail errorPdf(PdfGenerationException ex) {
        log.error("Error generando PDF", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo generar el PDF");
    }
}
