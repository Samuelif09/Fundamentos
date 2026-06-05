package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.PaymentMethodStatusRequest;
import com.openlib.market.frontend.model.SysCategory;
import com.openlib.market.frontend.model.SysCommission;
import com.openlib.market.frontend.model.SysPaymentMethod;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SystemConfigService {

    // ── Métodos de Pago ──────────────────────────────────────────────────

    public CompletableFuture<List<SysPaymentMethod>> getPaymentMethods() {
        return ApiClient.get("/admin/configuracion/metodos-pago", SysPaymentMethod[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<String>> updatePaymentMethodStatus(String id, String status) {
        PaymentMethodStatusRequest req = new PaymentMethodStatusRequest(status);
        // Assuming PATCH is supported or using POST as fallback
        return ApiClient.post("/admin/configuracion/metodos-pago/" + id + "/estado", req, String.class);
    }

    // ── Comisiones ───────────────────────────────────────────────────────

    public CompletableFuture<SysCommission> getCommissions() {
        return ApiClient.get("/admin/configuracion/comisiones", SysCommission.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<SysCommission>> updateCommissions(double percentage) {
        SysCommission req = new SysCommission(percentage);
        return ApiClient.put("/admin/configuracion/comisiones", req, SysCommission.class);
    }

    // ── Categorías ───────────────────────────────────────────────────────

    public CompletableFuture<List<SysCategory>> getCategories() {
        return ApiClient.get("/admin/configuracion/categorias", SysCategory[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<SysCategory>> createCategory(String name) {
        SysCategory req = new SysCategory(name);
        return ApiClient.post("/admin/configuracion/categorias", req, SysCategory.class);
    }

    public CompletableFuture<ApiResponse<String>> deleteCategory(String id) {
        return ApiClient.delete("/admin/configuracion/categorias/" + id, String.class);
    }
}
