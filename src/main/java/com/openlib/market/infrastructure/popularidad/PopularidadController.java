package com.openlib.market.infrastructure.popularidad;

import com.openlib.market.application.popularidad.IFiltrarPopularidadUseCase;
import com.openlib.market.application.popularidad.LibroPopularDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo")
public class PopularidadController {

    private final IFiltrarPopularidadUseCase filtrarPopularidadUseCase;

    public PopularidadController(IFiltrarPopularidadUseCase filtrarPopularidadUseCase) {
        this.filtrarPopularidadUseCase = filtrarPopularidadUseCase;
    }

    @GetMapping("/popularidad")
    public ResponseEntity<List<LibroPopularDto>> filtrarPorPopularidad() {
        List<LibroPopularDto> populares = filtrarPopularidadUseCase.filtrarPorPopularidad();
        return ResponseEntity.ok(populares);
    }
}
