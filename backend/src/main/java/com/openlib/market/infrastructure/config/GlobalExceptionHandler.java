package com.openlib.market.infrastructure.config;

import com.openlib.market.domain.detalle.LibroNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LibroNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleLibroNoEncontrado(LibroNoEncontradoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Recurso no encontrado");
        response.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(com.openlib.market.domain.comparte.LibroNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> handleLibroNoDisponible(com.openlib.market.domain.comparte.LibroNoDisponibleException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Libro no disponible");
        response.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Petición inválida");
        response.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(com.openlib.market.application.pago.PagoRechazadoException.class)
    public ResponseEntity<Map<String, String>> handlePagoRechazado(com.openlib.market.application.pago.PagoRechazadoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Pago Requerido / Rechazado");
        response.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
    }
}
