package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.Book;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookService {

    public CompletableFuture<List<Book>> getBooks(String query, String category) {
        StringBuilder endpoint = new StringBuilder("/libros?");
        
        if (query != null && !query.trim().isEmpty()) {
            endpoint.append("search=").append(URLEncoder.encode(query, StandardCharsets.UTF_8)).append("&");
        }
        if (category != null && !category.trim().isEmpty()) {
            endpoint.append("category=").append(URLEncoder.encode(category, StandardCharsets.UTF_8)).append("&");
        }
        
        // Remove trailing '&' or '?'
        String url = endpoint.toString();
        if (url.endsWith("&") || url.endsWith("?")) {
            url = url.substring(0, url.length() - 1);
        }

        return ApiClient.get(url, Book[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching books: " + response.getErrorMessage());
                });
    }

    public CompletableFuture<Book> getBookById(String id) {
        String url = "/libros/" + id;
        return ApiClient.get(url, Book.class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return response.getBody();
                    }
                    throw new RuntimeException("Error fetching book details: " + response.getErrorMessage());
                });
    }
}
