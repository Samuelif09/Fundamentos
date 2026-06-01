package com.openlib.market.infrastructure.almacenamiento;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadsController {

    private static final Path root = Paths.get("uploads");

    @GetMapping("/uploads/portadas/{isbn}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String isbn, @PathVariable String filename) {
        try {
            Path file = root.resolve("portadas").resolve(isbn).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                MediaType mediaType = MediaType.IMAGE_JPEG;
                if (filename.toLowerCase().endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else if (filename.toLowerCase().endsWith(".webp")) {
                    mediaType = MediaType.parseMediaType("image/webp");
                }
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
