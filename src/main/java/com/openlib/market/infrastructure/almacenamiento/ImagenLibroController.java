package com.openlib.market.infrastructure.almacenamiento;

import com.openlib.market.application.almacenamiento.ISubirImagenLibroUseCase;
import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/libros")
public class ImagenLibroController {

    private final ISubirImagenLibroUseCase subirImagenUseCase;

    public ImagenLibroController(ISubirImagenLibroUseCase subirImagenUseCase) {
        this.subirImagenUseCase = subirImagenUseCase;
    }

    @PostMapping(value = "/{id}/portada", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirPortada(@PathVariable String id, @RequestParam("archivo") MultipartFile file) {
        try {
            ArchivoImagen imagen = new ArchivoImagen(
                    file.getBytes(),
                    file.getContentType(),
                    file.getOriginalFilename()
            );
            String url = subirImagenUseCase.subirPortada(id, imagen);
            return ResponseEntity.ok("Portada subida: " + url);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al leer el archivo.");
        }
    }
}
