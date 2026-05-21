package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.CheckoutRequest;
import com.openlib.market.frontend.model.CheckoutResponse;

import java.util.concurrent.CompletableFuture;

public class CheckoutService {

    public CompletableFuture<CheckoutResponse> processCheckout(CheckoutRequest request) {
        String url = "/pedidos/checkout";
        return ApiClient.post(url, request, CheckoutResponse.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error processing checkout: " + response.getErrorMessage());
                });
    }
}
