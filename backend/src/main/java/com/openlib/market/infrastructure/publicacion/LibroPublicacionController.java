package com.openlib.market.infrastructure.publicacion;

import com.openlib.market.application.publicacion.IPublicarLibroUseCase;
import com.openlib.market.application.publicacion.PublicarLibroRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendedores")
public class LibroPublicacionController {

    private final IPublicarLibroUseCase publicarLibroUseCase;

    public LibroPublicacionController(IPublicarLibroUseCase publicarLibroUseCase) {
        this.publicarLibroUseCase = publicarLibroUseCase;
    }

    @PostMapping("/{sellerId}/libros")
    public ResponseEntity<String> publicarLibro(@PathVariable String sellerId, @RequestBody PublicarLibroRequestDto request) {
        // En una API real, se usaría el ID validado por el token JWT o Request DTO re-mapeado, aquí aseguramos la URL
        PublicarLibroRequestDto securedRequest = new PublicarLibroRequestDto(
                sellerId,
                request.getIsbn(),
                request.getTitulo(),
                request.getSinopsis(),
                request.getPrecio(),
                request.getUrlPortada(),
                request.getCategoria()
        );
        
        publicarLibroUseCase.publicar(securedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Libro publicado exitosamente.");
    }
}
