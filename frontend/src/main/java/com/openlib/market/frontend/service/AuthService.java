package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.LoginRequest;
import com.openlib.market.frontend.model.LoginResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Servicio de Autenticación. Comunica la vista con el backend de forma asíncrona.
 * Endpoint: POST /api/v1/auth/login
 */
public class AuthService {

    public CompletableFuture<ApiResponse<LoginResponse>> login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return ApiClient.postPublic("/auth/login", request, LoginResponse.class);
    }

    public CompletableFuture<ApiResponse<String>> recuperarPassword(String email) {
        record RecuperarPasswordRequest(String email) {}
        return ApiClient.postPublic("/auth/recuperar-password", new RecuperarPasswordRequest(email), String.class);
    }
}
