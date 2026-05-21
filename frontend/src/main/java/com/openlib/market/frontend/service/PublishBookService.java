package com.openlib.market.frontend.service;

import com.openlib.market.frontend.http.ApiClient;
import com.openlib.market.frontend.http.MultipartBodyBuilder;
import com.openlib.market.frontend.model.SellerBook;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

public class PublishBookService {

    /**
     * Publica un nuevo libro vía multipart/form-data.
     *
     * @param titulo          Título del libro
     * @param autor           Autor del libro
     * @param descripcion     Sinopsis/descripción
     * @param precio          Precio de venta
     * @param categoria       Categoría/género
     * @param isbn            Código ISBN
     * @param coverFile       Archivo de portada (imagen)
     * @param previewFile     Archivo de contenido (PDF o EPUB)
     */
    public CompletableFuture<com.openlib.market.frontend.http.ApiResponse<SellerBook>> publishBook(
            String titulo,
            String autor,
            String descripcion,
            String precio,
            String categoria,
            String isbn,
            File coverFile,
            File previewFile) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                MultipartBodyBuilder builder = new MultipartBodyBuilder();

                // Campos de texto
                builder.addField("titulo", titulo)
                       .addField("autor", autor)
                       .addField("descripcion", descripcion)
                       .addField("precio", precio)
                       .addField("categoria", categoria)
                       .addField("isbn", isbn);

                // Archivo de portada
                if (coverFile != null && coverFile.exists()) {
                    byte[] coverBytes = Files.readAllBytes(coverFile.toPath());
                    String coverMime  = detectMimeType(coverFile.getName(), "image/jpeg");
                    builder.addFile("portada", coverFile.getName(), coverMime, coverBytes);
                }

                // Archivo de contenido
                if (previewFile != null && previewFile.exists()) {
                    byte[] previewBytes = Files.readAllBytes(previewFile.toPath());
                    String previewMime  = detectMimeType(previewFile.getName(), "application/pdf");
                    builder.addFile("archivoPreview", previewFile.getName(), previewMime, previewBytes);
                }

                return new Object[]{ builder.build(), builder.getBoundary() };
            } catch (Exception e) {
                throw new RuntimeException("Error preparando el request multipart: " + e.getMessage(), e);
            }
        }).thenCompose(parts -> {
            var bodyPublisher = (java.net.http.HttpRequest.BodyPublisher) ((Object[]) parts)[0];
            var boundary      = (String) ((Object[]) parts)[1];
            return ApiClient.postMultipart("/vendedores/me/libros", bodyPublisher, boundary, SellerBook.class);
        });
    }

    private String detectMimeType(String filename, String defaultMime) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf"))  return "application/pdf";
        if (lower.endsWith(".epub")) return "application/epub+zip";
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".webp")) return "image/webp";
        return defaultMime;
    }
}
