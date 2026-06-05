package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.SellerFinance;
import com.openlib.market.frontend.model.SellerSalesMetrics;

import java.util.concurrent.CompletableFuture;

public class SellerDashboardService {

    public CompletableFuture<SellerFinance> getFinances() {
        String userId = com.openlib.market.frontend.session.SessionManager.getInstance().getUserId();
        return ApiClient.get("/vendedores/" + userId + "/dashboard/finanzas", SellerFinance.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching finances: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<SellerSalesMetrics> getMetrics() {
        String userId = com.openlib.market.frontend.session.SessionManager.getInstance().getUserId();
        return ApiClient.get("/vendedores/" + userId + "/dashboard/metricas", SellerSalesMetrics.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching metrics: " + response.getErrorMessage());
                });
    }
}
