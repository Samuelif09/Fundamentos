package com.openlib.market.frontend.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.Book;
import com.openlib.market.frontend.model.Cart;
import com.openlib.market.frontend.model.CartItem;
import com.openlib.market.frontend.session.SessionManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CartService {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BackendCartResponse {
        public String sesionId;
        public List<BackendCartItem> items;
        public double total;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BackendCartItem {
        public String isbn;
        public String nombreProducto;
        public int cantidad;
        public double precioUnitario;
    }

    private String getUserId() {
        String userId = SessionManager.getInstance().getUserId();
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("No hay sesión de usuario activa (falta UUID).");
        }
        return userId;
    }

    public CompletableFuture<Cart> getMyCart() {
        try {
            String userId = getUserId();
            // Ruta corregida: /api/v1/carrito está en ApiClient, pasamos /carrito/usuarios/{userId}/carrito
            String url = "/carrito/usuarios/" + userId + "/carrito";
            
            return ApiClient.get(url, BackendCartResponse.class)
                    .thenApply(response -> {
                        if (response.isSuccess() && response.getBody() != null) {
                            BackendCartResponse backendCart = response.getBody();
                            Cart frontendCart = new Cart();
                            
                            // 1. Mapeo del Listado de Ítems
                            if (backendCart.items != null) {
                                List<CartItem> mappedItems = backendCart.items.stream().map(bItem -> {
                                    Book dummyBook = new Book();
                                    dummyBook.setId(bItem.isbn);
                                    dummyBook.setTitle(bItem.nombreProducto);
                                    dummyBook.setPrice(bItem.precioUnitario);
                                    return new CartItem(bItem.isbn, dummyBook, bItem.cantidad);
                                }).collect(Collectors.toList());
                                frontendCart.setItems(mappedItems);
                            }
                            
                            // 2. Mapeo Total (El backend ya aplica Decorator con IVA)
                            // Hacemos el cálculo inverso para mostrar el desglose
                            double subtotal = backendCart.total / 1.19;
                            double taxes = backendCart.total - subtotal;
                            
                            frontendCart.setTotal(backendCart.total);
                            frontendCart.setSubtotal(subtotal);
                            frontendCart.setTaxes(taxes);

                            return frontendCart;
                        }
                        System.err.println("[CartService] GET /carrito falló. Status: " + response.getStatusCode() + " - " + response.getErrorMessage());
                        throw new RuntimeException("Error fetching cart: " + response.getErrorMessage());
                    })
                    .exceptionally(ex -> {
                        System.err.println("[CartService] Excepción de red en GET /carrito: " + ex.getMessage());
                        throw new RuntimeException(ex);
                    });
        } catch (Exception e) {
            System.err.println("[CartService] Excepción capturada en getMyCart: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> addToCart(String isbn, int quantity) {
        try {
            String userId = getUserId();
            // Ruta corregida: /api/v1/carrito/usuarios/{userId}/carrito/items
            String url = "/carrito/usuarios/" + userId + "/carrito/items";
            
            Map<String, Object> body = Map.of(
                    "idUsuario", userId,
                    "libroIsbn", isbn,
                    "cantidad", quantity
            );
            return ApiClient.post(url, body, String.class)
                    .thenAccept(response -> {
                        if (!response.isSuccess()) {
                            System.err.println("[CartService] POST /carrito/items falló. Status: " + response.getStatusCode() + " - " + response.getErrorMessage());
                            throw new RuntimeException(response.getErrorMessage());
                        }
                    })
                    .exceptionally(ex -> {
                        System.err.println("[CartService] Excepción de red en POST /carrito/items: " + ex.getMessage());
                        throw new RuntimeException(ex);
                    });
        } catch (Exception e) {
            System.err.println("[CartService] Excepción capturada en addToCart: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> updateQuantity(String isbn, int targetQuantity) {
        // Obtenemos el carrito actual para saber qué cantidad previa tiene este ítem
        return getMyCart().thenCompose(cart -> {
            int currentQty = 0;
            if (cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {
                    if (item.getBook() != null && isbn.equals(item.getBook().getId())) {
                        currentQty = item.getQuantity();
                        break;
                    }
                }
            }
            
            // Calculamos la diferencia (+1, -1, etc)
            int delta = targetQuantity - currentQty;
            try {
                String userId = getUserId();
                
                if (delta < 0) {
                    // WORKAROUND: Como el POST rechaza valores negativos (400),
                    // emulamos la resta eliminando el ítem y reinsertándolo con la cantidad absoluta deseada.
                    String deleteUrl = "/carrito/usuarios/" + userId + "/carrito/items/" + isbn;
                    return ApiClient.delete(deleteUrl, Void.class)
                            .thenCompose(deleteResp -> {
                                if (!deleteResp.isSuccess()) {
                                    throw new RuntimeException("Error al eliminar para actualizar: " + deleteResp.getErrorMessage());
                                }
                                if (targetQuantity > 0) {
                                    String postUrl = "/carrito/usuarios/" + userId + "/carrito/items";
                                    Map<String, Object> body = Map.of(
                                            "idUsuario", userId,
                                            "libroIsbn", isbn,
                                            "cantidad", targetQuantity
                                    );
                                    return ApiClient.post(postUrl, body, String.class).thenAccept(postResp -> {
                                        if (!postResp.isSuccess()) {
                                            throw new RuntimeException("Error al reinsertar cantidad: " + postResp.getErrorMessage());
                                        }
                                    });
                                }
                                return CompletableFuture.completedFuture(null);
                            });
                } else {
                    // Delta positivo: enviamos solo el incremento al POST
                    String url = "/carrito/usuarios/" + userId + "/carrito/items";
                    Map<String, Object> body = Map.of(
                            "idUsuario", userId,
                            "libroIsbn", isbn,
                            "cantidad", delta
                    );
                    return ApiClient.post(url, body, String.class)
                            .thenAccept(response -> {
                                if (!response.isSuccess()) {
                                    throw new RuntimeException("Error al incrementar cantidad: " + response.getErrorMessage());
                                }
                            });
                }
            } catch (Exception e) {
                System.err.println("[CartService] Excepción capturada en updateQuantity: " + e.getMessage());
                return CompletableFuture.failedFuture(e);
            }
        });
    }

    public CompletableFuture<Void> removeItem(String isbn) {
        try {
            String userId = getUserId();
            // Restituimos la llamada al endpoint de Postman
            String url = "/carrito/usuarios/" + userId + "/carrito/items/" + isbn;
            return ApiClient.delete(url, Void.class)
                    .thenAccept(response -> {
                        if (!response.isSuccess()) {
                            System.err.println("[CartService] DELETE /carrito/items falló. Status: " + response.getStatusCode() + " - " + response.getErrorMessage());
                            throw new RuntimeException(response.getErrorMessage());
                        }
                    })
                    .exceptionally(ex -> {
                        System.err.println("[CartService] Excepción de red en removeItem: " + ex.getMessage());
                        throw new RuntimeException(ex);
                    });
        } catch (Exception e) {
            System.err.println("[CartService] Excepción capturada en removeItem: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
}
