package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.SellerFinance;
import com.openlib.market.frontend.model.SellerSalesMetrics;

import java.util.concurrent.CompletableFuture;

public class SellerDashboardService {

    public CompletableFuture<SellerFinance> getFinances() {
        return ApiClient.get("/vendedores/me/dashboard/finanzas", SellerFinance.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching finances: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<SellerSalesMetrics> getMetrics() {
        return ApiClient.get("/vendedores/me/dashboard/metricas", SellerSalesMetrics.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching metrics: " + response.getErrorMessage());
                });
    }
}
