package com.openlib.market.infrastructure.comparte;

import com.openlib.market.application.comparte.EnlaceDto;
import com.openlib.market.application.comparte.ICompartirComparteUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/libros")
public class CompartirController {

    private final ICompartirComparteUseCase compartirUseCase;

    public CompartirController(ICompartirComparteUseCase compartirUseCase) {
        this.compartirUseCase = compartirUseCase;
    }

    @GetMapping("/{id}/compartir")
    public ResponseEntity<EnlaceDto> compartir(@PathVariable("id") String id) {
        EnlaceDto enlace = compartirUseCase.generarEnlace(id);
        return ResponseEntity.ok(enlace);
    }
}
