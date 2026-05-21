package com.openlib.market.infrastructure.explorar;

import com.openlib.market.application.explorar.IExplorarBusquedaUseCase;
import com.openlib.market.application.explorar.LibroTendenciaDto;
import com.openlib.market.domain.explorar.CriterioTendencia;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo")
public class ExplorarController {

    private final IExplorarBusquedaUseCase explorarUseCase;

    public ExplorarController(IExplorarBusquedaUseCase explorarUseCase) {
        this.explorarUseCase = explorarUseCase;
    }

    @GetMapping("/tendencias")
    public ResponseEntity<List<LibroTendenciaDto>> obtenerTendencias(
            @RequestParam(value = "criterio", defaultValue = "MAS_VENDIDOS") String criterioStr) {
        
        CriterioTendencia criterio;
        try {
            criterio = CriterioTendencia.valueOf(criterioStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        List<LibroTendenciaDto> tendencias = explorarUseCase.explorarTendencias(criterio);
        return ResponseEntity.ok(tendencias);
    }
}
