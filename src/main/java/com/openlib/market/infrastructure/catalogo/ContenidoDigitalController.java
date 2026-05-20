package com.openlib.market.infrastructure.catalogo;

import com.openlib.market.application.publicacion.IPublicarContenidoDigitalUseCase;
import com.openlib.market.application.publicacion.PublicarContenidoRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendedores/{idVendedor}/contenidos")
public class ContenidoDigitalController {

    private final IPublicarContenidoDigitalUseCase publicarContenidoDigitalUseCase;

    public ContenidoDigitalController(IPublicarContenidoDigitalUseCase publicarContenidoDigitalUseCase) {
        this.publicarContenidoDigitalUseCase = publicarContenidoDigitalUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> publicarContenido(
            @PathVariable String idVendedor,
            @RequestBody PublicarContenidoRequestDto request) {
        try {
            // El request DTO podría venir sin idVendedor en el payload, inyectarlo del path
            PublicarContenidoRequestDto fullRequest = new PublicarContenidoRequestDto(
                    request.isbn(),
                    request.titulo(),
                    request.sinopsis(),
                    request.precio(),
                    request.urlPortada(),
                    request.categoria(),
                    idVendedor,
                    request.tipoFormato(),
                    request.duracionMinutos()
            );
            
            publicarContenidoDigitalUseCase.publicar(fullRequest);
            return ResponseEntity.status(201).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
