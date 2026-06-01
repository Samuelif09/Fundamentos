package com.openlib.market.infrastructure.publicacion;

import com.openlib.market.application.publicacion.IPublicarLibroUseCase;
import com.openlib.market.application.publicacion.PublicarLibroRequestDto;
import com.openlib.market.application.almacenamiento.ISubirImagenLibroUseCase;
import com.openlib.market.application.publicacion.ISubirVistaPreviaUseCase;
import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/vendedores")
public class LibroPublicacionController {

    private final IPublicarLibroUseCase publicarLibroUseCase;
    private final ISubirImagenLibroUseCase subirImagenUseCase;
    private final ISubirVistaPreviaUseCase subirVistaPreviaUseCase;

    public LibroPublicacionController(
            IPublicarLibroUseCase publicarLibroUseCase,
            ISubirImagenLibroUseCase subirImagenUseCase,
            ISubirVistaPreviaUseCase subirVistaPreviaUseCase) {
        this.publicarLibroUseCase = publicarLibroUseCase;
        this.subirImagenUseCase = subirImagenUseCase;
        this.subirVistaPreviaUseCase = subirVistaPreviaUseCase;
    }

    @PostMapping(value = "/{sellerId}/libros", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> publicarLibro(
            @PathVariable String sellerId,
            @RequestParam("isbn") String isbn,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") double precio,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "stock", defaultValue = "10") int stock,
            @RequestParam(value = "portada", required = false) MultipartFile portada,
            @RequestParam(value = "archivoPreview", required = false) MultipartFile archivoPreview,
            HttpServletRequest httpRequest) {

        String realSellerId = sellerId;
        if ("me".equalsIgnoreCase(sellerId)) {
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String[] parts = token.split("\\.");
                if (parts.length > 1) {
                    realSellerId = parts[1];
                }
            }
        }

        String urlPortada = "";
        if (portada != null && !portada.isEmpty()) {
            try {
                ArchivoImagen archivoImagen = new ArchivoImagen(
                        portada.getBytes(),
                        portada.getContentType(),
                        portada.getOriginalFilename()
                );
                urlPortada = subirImagenUseCase.subirPortada(isbn, archivoImagen);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al subir la portada: " + e.getMessage());
            }
        }

        try {
            PublicarLibroRequestDto securedRequest = new PublicarLibroRequestDto(
                    realSellerId,
                    isbn,
                    titulo,
                    descripcion,
                    precio,
                    urlPortada,
                    categoria,
                    stock
            );
            publicarLibroUseCase.publicar(securedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al guardar metadatos: " + e.getMessage());
        }

        if (archivoPreview != null && !archivoPreview.isEmpty()) {
            try {
                subirVistaPreviaUseCase.subirVistaPrevia(
                        realSellerId,
                        isbn,
                        archivoPreview.getBytes(),
                        archivoPreview.getContentType()
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al subir el archivo de contenido: " + e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Libro publicado exitosamente.");
    }
}
