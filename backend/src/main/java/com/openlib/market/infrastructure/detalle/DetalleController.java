package com.openlib.market.infrastructure.detalle;

import com.openlib.market.application.detalle.IVerDetalleUseCase;
import com.openlib.market.application.detalle.LibroDetalleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/libros")
public class DetalleController {

    private final IVerDetalleUseCase verDetalleUseCase;

    public DetalleController(IVerDetalleUseCase verDetalleUseCase) {
        this.verDetalleUseCase = verDetalleUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDetalleDto> verDetalle(@PathVariable("id") String id) {
        LibroDetalleDto detalle = verDetalleUseCase.verDetalle(id);
        return ResponseEntity.ok(detalle);
    }
}
