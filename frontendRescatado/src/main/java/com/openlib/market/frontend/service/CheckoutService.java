package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.CheckoutRequest;
import com.openlib.market.frontend.model.CheckoutResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CheckoutService {

    public CompletableFuture<CheckoutResponse> processCheckout(CheckoutRequest request) {
        String userId = com.openlib.market.frontend.session.SessionManager.getInstance().getUserId();
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("No hay sesión de usuario activa (falta UUID).");
        }
        
        // 1. Usar el userId como sesionId (ya que el visitante se promueve a usuario)
        String sesionId = userId; 
        
        // 2. Ruta corregida (backend expone /api/v1/checkout/{sesionId})
        String url = "/checkout/" + sesionId;
        
        // 3. Mapeo del método de pago de cadena UI amigable a Enum estricto del backend
        // DESCUBRIMIENTO: El backend espera los valores del Enum TipoMetodoPago (TARJETA, PAYPAL, CRYPTO, TRANSFERENCIA)
        String rawPayment = request.getPaymentMethod();
        String parsedPayment = "TARJETA"; // Default fallback
        if (rawPayment != null) {
            String lower = rawPayment.toLowerCase();
            if (lower.contains("débito") || lower.contains("debito") || lower.contains("crédito") || lower.contains("credito") || lower.contains("tarjeta") || lower.contains("efectivo")) {
                // Asumiremos TARJETA incluso para "Efectivo" ya que no existe tal Enum en el backend.
                parsedPayment = "TARJETA";
            } else if (lower.contains("paypal")) {
                parsedPayment = "PAYPAL";
            } else if (lower.contains("crypto") || lower.contains("cripto")) {
                parsedPayment = "CRYPTO";
            } else if (lower.contains("transferencia")) {
                parsedPayment = "TRANSFERENCIA";
            } else {
                parsedPayment = "TARJETA"; // Fallback definitivo
            }
        }
        
        // 4. Mapeo de Request: el backend espera CheckoutRequestDTO (idUsuario, metodoPago)
        Map<String, String> backendRequest = Map.of(
                "idUsuario", userId,
                "metodoPago", parsedPayment
        );
        
        // El backend devuelve Void (vacío), no un JSON con CheckoutResponse
        return ApiClient.post(url, backendRequest, Void.class)
                .thenApply(response -> {
                    if (response.isSuccess()) {
                        // 4. Mapeo de Response: Dummy object para no romper la UI
                        CheckoutResponse frontendResponse = new CheckoutResponse();
                        frontendResponse.setOrderId("PED-" + System.currentTimeMillis());
                        frontendResponse.setStatus("COMPLETED");
                        frontendResponse.setMessage("Checkout procesado exitosamente por el backend.");
                        return frontendResponse;
                    }
                    throw new RuntimeException("Error processing checkout: " + response.getErrorMessage());
                });
    }
}
