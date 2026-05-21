package com.openlib.market.infrastructure.filtroprecio;

import com.openlib.market.application.filtroprecio.IFiltrarPorPrecioUseCase;
import com.openlib.market.application.filtroprecio.LibroBuscadoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo")
public class BusquedaPrecioController {

    private final IFiltrarPorPrecioUseCase filtrarPorPrecioUseCase;

    public BusquedaPrecioController(IFiltrarPorPrecioUseCase filtrarPorPrecioUseCase) {
        this.filtrarPorPrecioUseCase = filtrarPorPrecioUseCase;
    }

    @GetMapping("/filtro-precio")
    public ResponseEntity<List<LibroBuscadoDto>> filtrarPorPrecio(
            @RequestParam("min") double min,
            @RequestParam("max") double max) {
        try {
            List<LibroBuscadoDto> resultados = filtrarPorPrecioUseCase.filtrar(min, max);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
