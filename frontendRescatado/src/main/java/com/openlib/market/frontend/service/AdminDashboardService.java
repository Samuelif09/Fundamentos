package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.AdminChartData;
import com.openlib.market.frontend.model.AdminKpi;

import java.util.concurrent.CompletableFuture;

public class AdminDashboardService {

    public CompletableFuture<AdminKpi> getGlobalKpis() {
        return ApiClient.get("/admin/dashboard/kpis", AdminKpi.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching KPIs: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<AdminChartData> getChartData() {
        return ApiClient.get("/admin/dashboard/graficas", AdminChartData.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching chart data: " + response.getErrorMessage());
                });
    }
}
