package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.SupportTicket;
import com.openlib.market.frontend.model.TicketReplyRequest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SupportService {

    public CompletableFuture<List<SupportTicket>> getTickets() {
        return ApiClient.get("/admin/soporte/tickets", SupportTicket[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching tickets: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<String>> replyToTicket(String ticketId, String message) {
        TicketReplyRequest request = new TicketReplyRequest(message);
        return ApiClient.post("/admin/soporte/tickets/" + ticketId + "/responder", request, String.class);
    }

    public CompletableFuture<ApiResponse<String>> closeTicket(String ticketId) {
        return ApiClient.post("/admin/soporte/tickets/" + ticketId + "/cerrar", null, String.class);
    }
}
