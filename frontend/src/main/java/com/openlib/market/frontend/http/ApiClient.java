package com.openlib.market.frontend.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.frontend.session.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Cliente HTTP genérico. Maneja:
 * - Inyección automática del token JWT en cabeceras (cuando existe sesión)
 * - Serialización/deserialización JSON con Jackson
 * - Ejecución completamente asíncrona con CompletableFuture
 */
public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // ── POST sin autenticación (login, registro) ──────────────────────────
    public static <T> CompletableFuture<ApiResponse<T>> postPublic(
            String endpoint, Object body, Class<T> responseType) {
        return sendRequest(buildPostRequest(endpoint, body, false), responseType);
    }

    // ── POST con token JWT ───────────────────────────────────────────────
    public static <T> CompletableFuture<ApiResponse<T>> post(
            String endpoint, Object body, Class<T> responseType) {
        return sendRequest(buildPostRequest(endpoint, body, true), responseType);
    }

    // ── GET con token JWT ────────────────────────────────────────────────
    public static <T> CompletableFuture<ApiResponse<T>> get(
            String endpoint, Class<T> responseType) {
        return sendRequest(buildGetRequest(endpoint), responseType);
    }

    // ── PUT con token JWT ────────────────────────────────────────────────
    public static <T> CompletableFuture<ApiResponse<T>> put(
            String endpoint, Object body, Class<T> responseType) {
        return sendRequest(buildPutRequest(endpoint, body), responseType);
    }

    // ── DELETE con token JWT ─────────────────────────────────────────────
    public static <T> CompletableFuture<ApiResponse<T>> delete(
            String endpoint, Class<T> responseType) {
        return sendRequest(buildDeleteRequest(endpoint), responseType);
    }

    // ── POST multipart/form-data con token JWT ────────────────────────────
    public static <T> CompletableFuture<ApiResponse<T>> postMultipart(
            String endpoint,
            HttpRequest.BodyPublisher bodyPublisher,
            String boundary,
            Class<T> responseType) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(bodyPublisher)
                .timeout(Duration.ofSeconds(60)); // Archivos grandes, timeout amplio
        injectAuth(builder);
        return sendRequest(builder.build(), responseType);
    }

    // ── Internos ─────────────────────────────────────────────────────────

    private static HttpRequest buildPostRequest(String endpoint, Object body, boolean withAuth) {
        try {
            String json = MAPPER.writeValueAsString(body);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(15));
            if (withAuth) injectAuth(builder);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException("Error serializando el body", e);
        }
    }

    private static HttpRequest buildGetRequest(String endpoint) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET()
                .timeout(Duration.ofSeconds(15));
        injectAuth(builder);
        return builder.build();
    }

    private static HttpRequest buildPutRequest(String endpoint, Object body) {
        try {
            String json = MAPPER.writeValueAsString(body);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(15));
            injectAuth(builder);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException("Error serializando el body", e);
        }
    }

    private static HttpRequest buildDeleteRequest(String endpoint) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .DELETE()
                .timeout(Duration.ofSeconds(15));
        injectAuth(builder);
        return builder.build();
    }

    private static void injectAuth(HttpRequest.Builder builder) {
        String token = SessionManager.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }
    }

    private static <T> CompletableFuture<ApiResponse<T>> sendRequest(
            HttpRequest request, Class<T> responseType) {
        CompletableFuture<ApiResponse<T>> future = CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    String rawBody = response.body();
                    if (status >= 200 && status < 300) {
                        try {
                            T parsed = MAPPER.readValue(rawBody, responseType);
                            return ApiResponse.ok(status, parsed);
                        } catch (Exception e) {
                            // Respuesta exitosa pero sin body JSON (ej. 204)
                            return ApiResponse.<T>ok(status, null);
                        }
                    } else {
                        return ApiResponse.<T>error(status, rawBody);
                    }
                });
        return future.exceptionally(ex -> ApiResponse.<T>error(0, "Error de red: " + ex.getMessage()));
    }
}
