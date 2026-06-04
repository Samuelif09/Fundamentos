package com.openlib.market.infrastructure.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(com.openlib.market.domain.detalle.LibroNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleLibroNoEncontrado(
            com.openlib.market.domain.detalle.LibroNoEncontradoException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(com.openlib.market.domain.comparte.LibroNoDisponibleException.class)
    public ResponseEntity<Map<String, Object>> handleLibroNoDisponible(
            com.openlib.market.domain.comparte.LibroNoDisponibleException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(com.openlib.market.application.pago.PagoRechazadoException.class)
    public ResponseEntity<Map<String, Object>> handlePagoRechazado(
            com.openlib.market.application.pago.PagoRechazadoException ex) {
        return buildErrorResponse(HttpStatus.PAYMENT_REQUIRED, ex.getMessage());
    }

    @ExceptionHandler(com.openlib.market.domain.autenticacion.CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, Object>> handleCredencialesInvalidas(
            com.openlib.market.domain.autenticacion.CredencialesInvalidasException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(com.openlib.market.domain.autenticacion.AccesoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> handleAccesoDenegado(
            com.openlib.market.domain.autenticacion.AccesoDenegadoException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado.");
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
