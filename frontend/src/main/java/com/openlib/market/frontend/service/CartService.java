package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.Cart;
import com.openlib.market.frontend.session.SessionManager;

import java.util.concurrent.CompletableFuture;

public class CartService {

    private String getCartBaseUrl() {
        String userId = SessionManager.getInstance().getUserId();
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("No hay sesión activa para acceder al carrito.");
        }
        return "/carrito/usuarios/" + userId + "/carrito";
    }

    public CompletableFuture<Cart> getMyCart() {
        try {
            String url = getCartBaseUrl();
            return ApiClient.get(url, Cart.class)
                    .thenApply(response -> {
                        if (response.isSuccess() && response.getBody() != null) {
                            return response.getBody();
                        }
                        throw new RuntimeException("Error fetching cart: " + response.getErrorMessage());
                    });
        } catch (IllegalStateException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> addToCart(String isbn, int quantity) {
        try {
            String url = getCartBaseUrl() + "/items";
            java.util.Map<String, Object> body = java.util.Map.of(
                    "libroIsbn", isbn,
                    "cantidad", quantity
            );
            return ApiClient.post(url, body, String.class)
                    .thenAccept(response -> {
                        if (!response.isSuccess()) {
                            throw new RuntimeException(response.getErrorMessage());
                        }
                    });
        } catch (IllegalStateException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> updateQuantity(String isbn, int quantity) {
        try {
            String url = getCartBaseUrl() + "/items/" + isbn;
            java.util.Map<String, Object> body = java.util.Map.of(
                    "cantidad", quantity
            );
            return ApiClient.put(url, body, Void.class)
                    .thenAccept(response -> {
                        if (!response.isSuccess()) {
                            throw new RuntimeException(response.getErrorMessage());
                        }
                    });
        } catch (IllegalStateException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> removeItem(String isbn) {
        try {
            String url = getCartBaseUrl() + "/items/" + isbn;
            return ApiClient.delete(url, Void.class)
                    .thenAccept(response -> {
                        if (!response.isSuccess()) {
                            throw new RuntimeException(response.getErrorMessage());
                        }
                    });
        } catch (IllegalStateException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
