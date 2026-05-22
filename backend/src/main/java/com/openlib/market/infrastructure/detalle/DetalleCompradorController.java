package com.openlib.market.infrastructure.detalle;

import com.openlib.market.application.detalle.IVerDetalleLibroUseCase;
import com.openlib.market.application.detalle.LibroDetalleCompradorDto;
import com.openlib.market.domain.detalle.LibroNoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/libros")
public class DetalleCompradorController {

    private final IVerDetalleLibroUseCase verDetalleLibroUseCase;

    public DetalleCompradorController(IVerDetalleLibroUseCase verDetalleLibroUseCase) {
        this.verDetalleLibroUseCase = verDetalleLibroUseCase;
    }

    @GetMapping("/{id}/detalle-comprador")
    public ResponseEntity<?> verDetalleComprador(@PathVariable("id") String id) {
        try {
            LibroDetalleCompradorDto dto = verDetalleLibroUseCase.verDetalle(id);
            return ResponseEntity.ok(dto);
        } catch (LibroNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
