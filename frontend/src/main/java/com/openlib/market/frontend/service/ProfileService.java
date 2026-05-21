package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.OrderHistoryItem;
import com.openlib.market.frontend.model.UserProfile;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileService {

    public CompletableFuture<UserProfile> getProfile() {
        return ApiClient.get("/usuarios/me/perfil", UserProfile.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching profile: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<List<OrderHistoryItem>> getOrderHistory() {
        return ApiClient.get("/usuarios/me/pedidos", OrderHistoryItem[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching order history: " + response.getErrorMessage());
                });
    }
}
