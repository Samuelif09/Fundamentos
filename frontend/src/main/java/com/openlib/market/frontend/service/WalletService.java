package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.Transaction;
import com.openlib.market.frontend.model.WithdrawalRequest;
import com.openlib.market.frontend.session.SessionManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WalletService {

    public CompletableFuture<List<Transaction>> getTransactions() {
        String idVendedor = SessionManager.getInstance().getUserId();
        String endpoint = "/vendedores/" + idVendedor + "/finanzas/transacciones";

        return ApiClient.get(endpoint, Transaction[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error al cargar transacciones: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<String>> requestWithdrawal(double monto, String cuentaDestino) {
        String idVendedor = SessionManager.getInstance().getUserId();
        String endpoint = "/vendedores/" + idVendedor + "/finanzas/retiros";
        WithdrawalRequest request = new WithdrawalRequest(monto, cuentaDestino);
        return ApiClient.post(endpoint, request, String.class);
    }
}
