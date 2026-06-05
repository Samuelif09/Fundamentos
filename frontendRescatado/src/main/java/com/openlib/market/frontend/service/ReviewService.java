package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.Review;
import com.openlib.market.frontend.model.ReviewRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReviewService {

    public CompletableFuture<List<Review>> getReviews(String isbn) {
        String url = "/libros/" + isbn + "/resenas";
        return ApiClient.get(url, Review[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return List.of(response.getBody());
                    }
                    throw new RuntimeException("Error fetching reviews: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<Void> postReview(String isbn, ReviewRequest request) {
        String url = "/libros/" + isbn + "/resenas";
        return ApiClient.post(url, request, Void.class)
                .thenApply(response -> {
                    if (response.isSuccess()) {
                        return null;
                    }
                    throw new RuntimeException("Error posting review: " + response.getErrorMessage());
                });
    }
}
