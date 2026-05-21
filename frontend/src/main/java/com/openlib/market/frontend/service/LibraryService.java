package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.model.Book;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LibraryService {

    public CompletableFuture<List<Book>> getMyLibrary() {
        // Alias "me" para el usuario activo de la sesión
        String url = "/usuarios/me/biblioteca";
        
        return ApiClient.get(url, Book[].class)
                .thenApply(response -> {
                    if (response.isSuccess() && response.getBody() != null) {
                        return Arrays.asList(response.getBody());
                    }
                    throw new RuntimeException("Error fetching library: " + response.getErrorMessage());
                });
    }
}
