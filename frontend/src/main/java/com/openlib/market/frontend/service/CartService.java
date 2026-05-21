package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.Cart;

import java.util.concurrent.CompletableFuture;

public class CartService {

    public CompletableFuture<Cart> getMyCart() {
        // Asumiendo que el ID del usuario se maneja como "me" en la sesión activa para el token inyectado
        String url = "/usuarios/me/carrito";
        return ApiClient.get(url, Cart.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching cart: " + response.getErrorMessage());
                });
    }
}
