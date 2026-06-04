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
    private final com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository vendedorRepository;

    public LibroPublicacionController(IPublicarLibroUseCase publicarLibroUseCase,
                                      com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository vendedorRepository) {
        this.publicarLibroUseCase = publicarLibroUseCase;
        this.vendedorRepository = vendedorRepository;
    }

    @PostMapping(value = "/{sellerId}/libros", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> publicarLibro(
            @PathVariable String sellerId,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") double precio,
            @RequestParam("categoria") String categoria,
            @RequestParam("isbn") String isbn,
            @RequestParam(value = "autor", required = false) String autor,
            @RequestParam(value = "stock", required = false) String stock,
            @RequestPart(value = "portada", required = false) org.springframework.web.multipart.MultipartFile portada,
            @RequestPart(value = "archivoPreview", required = false) org.springframework.web.multipart.MultipartFile archivoPreview) {
        
        // Traducir idUsuario a idVendedor si es necesario
        String realIdVendedor = vendedorRepository.findByIdUsuario(sellerId)
                .map(v -> v.getId())
                .orElse(sellerId);

        // Simulamos guardado o asignamos un default
        String urlPortada = (portada != null && !portada.isEmpty()) ? "/portadas/" + portada.getOriginalFilename() : "https://via.placeholder.com/150";

        PublicarLibroRequestDto securedRequest = new PublicarLibroRequestDto(
                realIdVendedor,
                isbn,
                titulo,
                descripcion,
                precio,
                urlPortada,
                categoria
        );
        
        publicarLibroUseCase.publicar(securedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Libro publicado exitosamente.");
    }
}
