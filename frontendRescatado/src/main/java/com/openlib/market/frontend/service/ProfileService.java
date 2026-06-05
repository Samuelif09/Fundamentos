package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.OrderHistoryItem;
import com.openlib.market.frontend.model.UserProfile;
import com.openlib.market.frontend.session.SessionManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileService {

    public CompletableFuture<UserProfile> getProfile() {
        String email  = SessionManager.getInstance().getEmail();
        String userId = SessionManager.getInstance().getUserId();

        // El backend tiene el controller en /api/v1/usuarios/me/perfil
        // pero el MeResolveFilter no decodifica el JWT correctamente.
        // Usamos el userId real en la URL y el email como fallback en query param.
        String path = userId != null && !userId.isBlank()
                ? "/usuarios/" + userId + "/perfil"
                : "/usuarios/me/perfil";

        String url = path + (email != null && !email.isBlank() ? "?email=" + email : "");

        return ApiClient.get(url, UserProfile.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    // Fallback: construir perfil básico desde la sesión
                    UserProfile fallback = new UserProfile();
                    fallback.setEmail(email != null ? email : "");
                    fallback.setFullName(email != null ? email.split("@")[0] : "Usuario");
                    fallback.setJoinedDate("-");
                    return fallback;
                });
    }

    public CompletableFuture<List<OrderHistoryItem>> getOrderHistory() {
        String email  = SessionManager.getInstance().getEmail();
        String userId = SessionManager.getInstance().getUserId();

        String path = userId != null && !userId.isBlank()
                ? "/usuarios/" + userId + "/pedidos"
                : "/usuarios/me/pedidos";

        String url = path + (email != null && !email.isBlank() ? "?email=" + email : "");

        return ApiClient.get(url, OrderHistoryItem[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    // Si no hay pedidos o falla, retorna lista vacía (no bloquea la UI)
                    return java.util.Collections.emptyList();
                });
    }
}
