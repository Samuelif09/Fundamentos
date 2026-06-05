package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.SellerBook;
import com.openlib.market.frontend.session.SessionManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SellerInventoryService {

    public CompletableFuture<List<SellerBook>> getInventory() {
        String userId = SessionManager.getInstance().getUserId();
        if (userId == null || userId.isBlank()) {
            return CompletableFuture.failedFuture(new RuntimeException("No hay sesión activa para obtener el inventario."));
        }
        String url = "/vendedores/" + userId + "/libros";
        return ApiClient.get(url, SellerBook[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching inventory: " + response.getErrorMessage());
                });
    }
}
