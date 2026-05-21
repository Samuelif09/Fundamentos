package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.PendingBook;
import com.openlib.market.frontend.model.RejectReasonRequest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CurationService {

    public CompletableFuture<List<PendingBook>> getPendingBooks() {
        return ApiClient.get("/admin/curaduria/libros-pendientes", PendingBook[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching pending books: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<String>> approveBook(String id) {
        return ApiClient.post("/admin/curaduria/libros/" + id + "/aprobar", null, String.class);
    }

    public CompletableFuture<ApiResponse<String>> rejectBook(String id, String reason) {
        RejectReasonRequest request = new RejectReasonRequest(reason);
        return ApiClient.post("/admin/curaduria/libros/" + id + "/rechazar", request, String.class);
    }
}
