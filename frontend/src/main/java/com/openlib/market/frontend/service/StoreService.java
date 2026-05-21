package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.http.MultipartBodyBuilder;
import com.openlib.market.frontend.model.AffiliateLink;
import com.openlib.market.frontend.model.StoreProfile;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StoreService {

    // ── Store profile ──────────────────────────────────────────────────────

    public CompletableFuture<StoreProfile> getStoreProfile() {
        return ApiClient.get("/vendedores/me/tienda", StoreProfile.class)
                .thenApply(r -> {
                    if (r.isSuccess() && r.getBody() != null) return r.getBody();
                    throw new RuntimeException("Error fetching store: " + r.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<StoreProfile>> updateStoreProfile(StoreProfile profile) {
        return ApiClient.put("/vendedores/me/tienda", profile, StoreProfile.class);
    }

    // ── Banner (multipart) ─────────────────────────────────────────────────

    public CompletableFuture<ApiResponse<String>> uploadBanner(File bannerFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] bytes = Files.readAllBytes(bannerFile.toPath());
                String mime  = bannerFile.getName().toLowerCase().endsWith(".png")
                        ? "image/png" : "image/jpeg";

                MultipartBodyBuilder builder = new MultipartBodyBuilder();
                builder.addFile("banner", bannerFile.getName(), mime, bytes);

                return new Object[]{ builder.build(), builder.getBoundary() };
            } catch (Exception e) {
                throw new RuntimeException("Error preparing banner upload: " + e.getMessage(), e);
            }
        }).thenCompose(parts -> {
            var pub      = (java.net.http.HttpRequest.BodyPublisher) ((Object[]) parts)[0];
            var boundary = (String) ((Object[]) parts)[1];
            return ApiClient.postMultipart("/vendedores/me/tienda/banner", pub, boundary, String.class);
        });
    }

    // ── Affiliate links ────────────────────────────────────────────────────

    public CompletableFuture<List<AffiliateLink>> getAffiliateLinks() {
        return ApiClient.get("/vendedores/me/tienda/afiliados", AffiliateLink[].class)
                .thenApply(r -> {
                    if (r.isSuccess() && r.getBody() != null) return Arrays.asList(r.getBody());
                    throw new RuntimeException("Error fetching links: " + r.getErrorMessage());
                });
    }

    public CompletableFuture<ApiResponse<AffiliateLink>> createAffiliateLink(AffiliateLink link) {
        return ApiClient.post("/vendedores/me/tienda/afiliados", link, AffiliateLink.class);
    }

    public CompletableFuture<ApiResponse<String>> deleteAffiliateLink(String id) {
        return ApiClient.delete("/vendedores/me/tienda/afiliados/" + id, String.class);
    }
}
