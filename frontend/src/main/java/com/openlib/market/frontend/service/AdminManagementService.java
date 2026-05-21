package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.AdminOrder;
import com.openlib.market.frontend.model.AdminUser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdminManagementService {

    // ── Usuarios ─────────────────────────────────────────────────────────

    public CompletableFuture<List<AdminUser>> getUsers() {
        return ApiClient.get("/admin/usuarios", AdminUser[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error al obtener usuarios: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<String>> suspendUser(String id) {
        return ApiClient.post("/admin/usuarios/" + id + "/suspender", null, String.class);
    }

    public CompletableFuture<ApiResponse<String>> reactivateUser(String id) {
        return ApiClient.post("/admin/usuarios/" + id + "/reactivar", null, String.class);
    }

    // ── Transacciones ────────────────────────────────────────────────────

    public CompletableFuture<List<AdminOrder>> getOrders() {
        return ApiClient.get("/admin/pedidos", AdminOrder[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error al obtener pedidos: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<String>> refundOrder(String id) {
        return ApiClient.post("/admin/pedidos/" + id + "/reembolsar", null, String.class);
    }
}
