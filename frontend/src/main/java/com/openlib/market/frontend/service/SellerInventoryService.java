package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.SellerBook;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SellerInventoryService {

    public CompletableFuture<List<SellerBook>> getInventory() {
        return ApiClient.get("/vendedores/me/libros", SellerBook[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching inventory: " + response.getErrorMessage());
                });
    }
}
