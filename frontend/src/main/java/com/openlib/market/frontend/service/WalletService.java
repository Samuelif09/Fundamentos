package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.Transaction;
import com.openlib.market.frontend.model.WithdrawalRequest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WalletService {

    public CompletableFuture<List<Transaction>> getTransactions() {
        return ApiClient.get("/vendedores/me/finanzas/transacciones", Transaction[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching transactions: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<String>> requestWithdrawal(double monto) {
        WithdrawalRequest request = new WithdrawalRequest(monto);
        return ApiClient.post("/vendedores/me/finanzas/retiros", request, String.class);
    }
}
